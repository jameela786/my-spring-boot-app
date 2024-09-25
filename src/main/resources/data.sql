-- Create the user table
CREATE TABLE IF NOT EXISTS user (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(255) NOT NULL,
  mob_num VARCHAR(20) NOT NULL,
  pan_num VARCHAR(10) NOT NULL,
  manager_id INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT true
);

-- Insert sample data into the user table
INSERT INTO user (full_name, mob_num, pan_num, manager_id)
VALUES ('John Doe', '1234567890', 'ABCDE1234F', NULL),
       ('Jane Smith', '9876543210', 'FGHIJ5678K', 1),
       ('Michael Johnson', '5555555555', 'LMNOP9012Q', 1);