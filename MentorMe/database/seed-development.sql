-- Optional local development seed data. Do not use real personal data.
USE mentorme_db;

INSERT INTO faculty (name) VALUES
('Faculty of Computer and Mathematical Sciences'),
('Faculty of Business and Management'),
('Faculty of Accountancy')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Create the first ADMIN account through a one-time secure bootstrap process.
-- Never store a plain-text password or a reusable production hash in this file.
