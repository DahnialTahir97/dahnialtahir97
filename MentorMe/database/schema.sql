-- MentorMe database schema
-- Source: user's final ERD_MentorMe.pdf (8 tables).
-- Run this script in MySQL Workbench. It creates an empty database schema.

CREATE DATABASE IF NOT EXISTS mentorme_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mentorme_db;

CREATE TABLE faculty (
    faculty_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (faculty_id),
    UNIQUE KEY uq_faculty_name (name)
) ENGINE=InnoDB;

CREATE TABLE users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    faculty_id BIGINT NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_users_email (email),
    KEY idx_users_faculty (faculty_id),
    CONSTRAINT fk_users_faculty FOREIGN KEY (faculty_id)
        REFERENCES faculty (faculty_id),
    CONSTRAINT chk_users_role CHECK (role IN ('STUDENT', 'TUTOR', 'ADMIN'))
) ENGINE=InnoDB;

CREATE TABLE subject (
    subject_id BIGINT NOT NULL AUTO_INCREMENT,
    subject_code VARCHAR(20) NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    faculty_id BIGINT NOT NULL,
    PRIMARY KEY (subject_id),
    UNIQUE KEY uq_subject_code (subject_code),
    KEY idx_subject_faculty (faculty_id),
    CONSTRAINT fk_subject_faculty FOREIGN KEY (faculty_id)
        REFERENCES faculty (faculty_id)
) ENGINE=InnoDB;

CREATE TABLE tutor_profile (
    tutor_profile_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    bio TEXT,
    hourly_rate DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (tutor_profile_id),
    UNIQUE KEY uq_tutor_profile_user (user_id),
    CONSTRAINT fk_tutor_profile_user FOREIGN KEY (user_id)
        REFERENCES users (user_id),
    CONSTRAINT chk_tutor_hourly_rate CHECK (hourly_rate >= 0)
) ENGINE=InnoDB;

CREATE TABLE tutor_subject (
    tutor_subject_id BIGINT NOT NULL AUTO_INCREMENT,
    tutor_profile_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    PRIMARY KEY (tutor_subject_id),
    UNIQUE KEY uq_tutor_subject_pair (tutor_profile_id, subject_id),
    CONSTRAINT fk_tutor_subject_profile FOREIGN KEY (tutor_profile_id)
        REFERENCES tutor_profile (tutor_profile_id),
    CONSTRAINT fk_tutor_subject_subject FOREIGN KEY (subject_id)
        REFERENCES subject (subject_id)
) ENGINE=InnoDB;

CREATE TABLE availability (
    availability_id BIGINT NOT NULL AUTO_INCREMENT,
    tutor_profile_id BIGINT NOT NULL,
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    PRIMARY KEY (availability_id),
    KEY idx_availability_tutor_day (tutor_profile_id, day_of_week),
    CONSTRAINT fk_availability_profile FOREIGN KEY (tutor_profile_id)
        REFERENCES tutor_profile (tutor_profile_id),
    CONSTRAINT chk_availability_day CHECK
        (day_of_week IN ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY')),
    CONSTRAINT chk_availability_time CHECK (end_time > start_time)
) ENGINE=InnoDB;

CREATE TABLE booking (
    booking_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    tutor_profile_id BIGINT NOT NULL,
    availability_id BIGINT NOT NULL,
    session_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    PRIMARY KEY (booking_id),
    KEY idx_booking_student (student_id),
    KEY idx_booking_tutor (tutor_profile_id),
    KEY idx_booking_slot_date (availability_id, session_date),
    CONSTRAINT fk_booking_student FOREIGN KEY (student_id)
        REFERENCES users (user_id),
    CONSTRAINT fk_booking_tutor_profile FOREIGN KEY (tutor_profile_id)
        REFERENCES tutor_profile (tutor_profile_id),
    CONSTRAINT fk_booking_availability FOREIGN KEY (availability_id)
        REFERENCES availability (availability_id),
    CONSTRAINT chk_booking_status CHECK
        (status IN ('PENDING','APPROVED','COMPLETED','CANCELLED'))
) ENGINE=InnoDB;

CREATE TABLE review (
    review_id BIGINT NOT NULL AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    rating TINYINT NOT NULL,
    comment TEXT,
    PRIMARY KEY (review_id),
    UNIQUE KEY uq_review_booking (booking_id),
    CONSTRAINT fk_review_booking FOREIGN KEY (booking_id)
        REFERENCES booking (booking_id),
    CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB;
