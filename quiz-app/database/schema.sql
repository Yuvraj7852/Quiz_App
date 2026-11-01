CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(500) NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL
);

INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
('Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B'),
('What is the default value of an int instance variable?', '0', 'null', '1', 'undefined', 'A'),
('Which collection does not allow duplicate elements?', 'ArrayList', 'LinkedList', 'HashSet', 'Vector', 'C'),
('Which of these is a checked exception?', 'NullPointerException', 'ArithmeticException', 'IOException', 'ArrayIndexOutOfBoundsException', 'C'),
('What does JVM stand for?', 'Java Variable Machine', 'Java Virtual Machine', 'Joint Virtual Method', 'Java Verified Machine', 'B'),
('Which of these is NOT a pillar of OOP?', 'Encapsulation', 'Polymorphism', 'Compilation', 'Inheritance', 'C'),
('Which interface is implemented by HashMap?', 'List', 'Set', 'Map', 'Queue', 'C'),
('Which keyword prevents a method from being overridden?', 'static', 'abstract', 'final', 'const', 'C'),
('Which JDBC interface helps prevent SQL injection?', 'Statement', 'PreparedStatement', 'Connection', 'ResultSet', 'B'),
('What is the output of 10 / 3 for two int values in Java?', '3.33', '3', '4', '3.0', 'B');
