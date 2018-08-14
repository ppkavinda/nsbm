-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 14, 2018 at 02:41 PM
-- Server version: 10.1.29-MariaDB
-- PHP Version: 7.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nsbm_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `al_result`
--

CREATE TABLE `al_result` (
  `student_id` int(11) NOT NULL,
  `sub1` varchar(2) NOT NULL,
  `sub2` varchar(2) NOT NULL,
  `sub3` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `al_result`
--

INSERT INTO `al_result` (`student_id`, `sub1`, `sub2`, `sub3`) VALUES
(3, '54', '45', '45');

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `course_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `duration` int(11) NOT NULL,
  `credit_limit` int(11) NOT NULL,
  `type` enum('M','B') NOT NULL,
  `faculty_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`course_id`, `name`, `duration`, `credit_limit`, `type`, `faculty_id`) VALUES
(1, 'Information System', 3, 3, 'B', 1),
(2, 'Computer Science', 3, 3, 'B', 1),
(3, 'Software Engineering', 4, 3, 'M', 1),
(4, 'Marketing Management', 3, 3, 'B', 2),
(5, 'Accounting ', 3, 3, 'B', 2),
(7, 'Human Resource Management', 3, 3, 'B', 2),
(8, 'Business Administration', 3, 3, 'M', 2),
(9, 'Civil Engineering', 4, 3, 'B', 3),
(10, 'Mechanical Engineering', 4, 3, 'B', 3),
(11, 'Electrical and Electronic Engineering', 4, 3, 'B', 3),
(12, 'quantity surveying', 4, 3, 'M', 3);

-- --------------------------------------------------------

--
-- Table structure for table `faculty`
--

CREATE TABLE `faculty` (
  `faculty_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `faculty`
--

INSERT INTO `faculty` (`faculty_id`, `name`) VALUES
(1, ' School of Computing'),
(2, 'School of Business'),
(3, ' School of Engineering');

-- --------------------------------------------------------

--
-- Table structure for table `instructor`
--

CREATE TABLE `instructor` (
  `instructor_id` int(11) NOT NULL,
  `fname` varchar(255) NOT NULL,
  `lname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `gender` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `instructor`
--

INSERT INTO `instructor` (`instructor_id`, `fname`, `lname`, `email`, `password`, `gender`) VALUES
(7, 'ins', 'tructor', 'ins@tructor.com', 'f7c72ef23871cc01d1cb315e4b336b', 'M');

-- --------------------------------------------------------

--
-- Table structure for table `instructor_practicle`
--

CREATE TABLE `instructor_practicle` (
  `staff_id` int(11) NOT NULL,
  `practicle_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `instructor_practicle`
--

INSERT INTO `instructor_practicle` (`staff_id`, `practicle_id`) VALUES
(7, 1);

-- --------------------------------------------------------

--
-- Table structure for table `lab`
--

CREATE TABLE `lab` (
  `lab_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lab`
--

INSERT INTO `lab` (`lab_id`, `name`) VALUES
(1, 'Lab A'),
(2, 'Lab B'),
(3, 'Lab C'),
(4, 'Lab D'),
(5, 'Lab E');

-- --------------------------------------------------------

--
-- Table structure for table `lecture`
--

CREATE TABLE `lecture` (
  `lecture_id` int(11) NOT NULL,
  `start_time` varchar(255) NOT NULL,
  `end_time` varchar(255) NOT NULL,
  `hall_id` int(11) NOT NULL,
  `subject_code` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lecture`
--

INSERT INTO `lecture` (`lecture_id`, `start_time`, `end_time`, `hall_id`, `subject_code`) VALUES
(1, 'Wednesday 08.00 am', 'Wednesday 10.00 am', 1, 10),
(2, 'Monday 10.00 am', 'Monday 12.00 am', 3, 16);

-- --------------------------------------------------------

--
-- Table structure for table `lecturer`
--

CREATE TABLE `lecturer` (
  `lecturer_id` int(11) NOT NULL,
  `fname` varchar(255) NOT NULL,
  `lname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `gender` enum('M','F') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lecturer`
--

INSERT INTO `lecturer` (`lecturer_id`, `fname`, `lname`, `email`, `password`, `gender`) VALUES
(8, 'lec', 'turer', 'lec@turer.com', '912ec83b2ce49e4a54168d495ab570', 'M');

-- --------------------------------------------------------

--
-- Table structure for table `lecturer_lec`
--

CREATE TABLE `lecturer_lec` (
  `staff_id` int(11) NOT NULL,
  `lec_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lecturer_lec`
--

INSERT INTO `lecturer_lec` (`staff_id`, `lec_id`) VALUES
(8, 1);

-- --------------------------------------------------------

--
-- Table structure for table `lec_hall`
--

CREATE TABLE `lec_hall` (
  `hall_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lec_hall`
--

INSERT INTO `lec_hall` (`hall_id`, `name`) VALUES
(1, 'Mini Auditorium'),
(2, 'W001'),
(3, 'W002'),
(4, '4th Floor'),
(5, 'Irque Lab');

-- --------------------------------------------------------

--
-- Table structure for table `postgraduate`
--

CREATE TABLE `postgraduate` (
  `student_id` int(11) NOT NULL,
  `qualification_type` varchar(255) NOT NULL,
  `institute` varchar(255) NOT NULL,
  `year_of_completion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `postgraduate`
--

INSERT INTO `postgraduate` (`student_id`, `qualification_type`, `institute`, `year_of_completion`) VALUES
(5, '98', '89', 89);

-- --------------------------------------------------------

--
-- Table structure for table `practicle`
--

CREATE TABLE `practicle` (
  `practicle_id` int(11) NOT NULL,
  `start_time` varchar(255) NOT NULL,
  `end_time` varchar(255) NOT NULL,
  `lab` int(11) NOT NULL,
  `subject_code` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `practicle`
--

INSERT INTO `practicle` (`practicle_id`, `start_time`, `end_time`, `lab`, `subject_code`) VALUES
(1, '10.00', '12.00', 2, 14);

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`role_id`, `role`) VALUES
(2, 'ug'),
(3, 'pg');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `student_id` int(11) NOT NULL,
  `fname` varchar(255) NOT NULL,
  `lname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address1` varchar(255) NOT NULL,
  `address2` varchar(255) NOT NULL,
  `telephone` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  `dob` date NOT NULL,
  `gender` enum('M','F','','') NOT NULL,
  `fac_id` int(11) DEFAULT NULL,
  `course_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`student_id`, `fname`, `lname`, `email`, `address1`, `address2`, `telephone`, `password`, `dob`, `gender`, `fac_id`, `course_id`) VALUES
(2, 'prasad', 'kavinda', 'h1628461@nwytg.com', 'medawatta kerantholla thelijjawila', 'srilanka', 23424, 'asdf', '2018-08-01', 'M', NULL, NULL),
(3, '54', '54', '54', '54', '54', 54, '6c8349cc7260ae62e3b1396831a8398f', '2018-07-31', 'F', 1, 2),
(5, '89', '98', '98', '98', '98', 89, '7647966b7343c29048673252e490f736', '2018-08-08', 'M', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `student_subject`
--

CREATE TABLE `student_subject` (
  `student_id` int(11) NOT NULL,
  `subject_code` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `student_test`
--

CREATE TABLE `student_test` (
  `student_id` int(11) NOT NULL,
  `test_id` int(11) NOT NULL,
  `grade` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `subject`
--

CREATE TABLE `subject` (
  `subject_code` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `credits` int(11) NOT NULL,
  `fee` double NOT NULL,
  `course_id` int(11) NOT NULL,
  `sem` int(11) NOT NULL,
  `compulsory` int(11) NOT NULL,
  `type` enum('UG','PG') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subject`
--

INSERT INTO `subject` (`subject_code`, `name`, `credits`, `fee`, `course_id`, `sem`, `compulsory`, `type`) VALUES
(1, 'Principles of Management', 3, 4000, 4, 1, 1, 'UG'),
(2, 'Financial Accounting', 3, 4000, 4, 1, 1, 'UG'),
(3, 'Mathematics for Business', 3, 4000, 4, 1, 1, 'UG'),
(4, 'Business Law', 3, 4000, 4, 1, 1, 'UG'),
(5, 'Information Technology I ', 3, 4000, 4, 1, 1, 'UG'),
(6, 'Microeconomics', 3, 4000, 4, 2, 1, 'UG'),
(7, 'Business Statistics', 3, 4000, 4, 2, 1, 'UG'),
(8, 'Cost & Management Accounting', 3, 4000, 4, 2, 1, 'PG'),
(9, 'Information Technology II', 3, 4000, 4, 2, 1, 'UG'),
(10, 'algo I', 3, 4000, 2, 1, 1, 'UG'),
(11, 'computer systems', 3, 4000, 2, 1, 1, 'UG'),
(12, 'database I', 3, 4000, 2, 1, 1, 'UG'),
(13, 'Mathamatical methods I', 3, 4000, 2, 1, 1, 'UG'),
(14, 'Laboratory I', 2, 4000, 2, 2, 0, 'UG'),
(15, 'Algo II', 3, 4000, 2, 2, 1, 'PG'),
(16, 'Software Engineering I', 3, 4000, 2, 2, 1, 'PG');

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE `test` (
  `test_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `start_time` varchar(255) NOT NULL,
  `end_time` varchar(255) NOT NULL,
  `subject_code` int(11) NOT NULL,
  `type` enum('PA','WA','EX') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `test`
--

INSERT INTO `test` (`test_id`, `date`, `start_time`, `end_time`, `subject_code`, `type`) VALUES
(1, '2018-08-08', '09.00 am', '11.00 am', 15, 'EX');

-- --------------------------------------------------------

--
-- Table structure for table `undergraduate`
--

CREATE TABLE `undergraduate` (
  `student_id` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `z_score` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `undergraduate`
--

INSERT INTO `undergraduate` (`student_id`, `rank`, `z_score`) VALUES
(2, 234, 3.2342),
(3, 54, 54);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`, `role`) VALUES
(1, 'admin@admin.com', '21232f297a57a5a743894a0e4a801fc3', 1),
(2, 'pp.kavinda@gmail.com', 'c246ad314ab52745b71bb00f4608c82a', 2),
(3, '54', '6c8349cc7260ae62e3b1396831a8398f', 4),
(5, '98', '7647966b7343c29048673252e490f736', 5),
(6, 'ins@tructor.com', 'f7c72ef23871cc01d1cb315e4b336b', 3),
(7, 'ins@tructor.com', 'f7c72ef23871cc01d1cb315e4b336b', 3),
(8, 'lec@turer.com', '912ec83b2ce49e4a54168d495ab570', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `al_result`
--
ALTER TABLE `al_result`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`course_id`),
  ADD KEY `course_ibfk_1` (`faculty_id`);

--
-- Indexes for table `faculty`
--
ALTER TABLE `faculty`
  ADD PRIMARY KEY (`faculty_id`);

--
-- Indexes for table `instructor`
--
ALTER TABLE `instructor`
  ADD PRIMARY KEY (`instructor_id`);

--
-- Indexes for table `instructor_practicle`
--
ALTER TABLE `instructor_practicle`
  ADD PRIMARY KEY (`staff_id`,`practicle_id`),
  ADD KEY `instructor_ibfk_2` (`staff_id`),
  ADD KEY `instructor_ibfk_1` (`practicle_id`);

--
-- Indexes for table `lab`
--
ALTER TABLE `lab`
  ADD PRIMARY KEY (`lab_id`);

--
-- Indexes for table `lecture`
--
ALTER TABLE `lecture`
  ADD PRIMARY KEY (`lecture_id`),
  ADD KEY `hall_id` (`hall_id`),
  ADD KEY `lecture_ibfk_2` (`subject_code`);

--
-- Indexes for table `lecturer`
--
ALTER TABLE `lecturer`
  ADD PRIMARY KEY (`lecturer_id`);

--
-- Indexes for table `lecturer_lec`
--
ALTER TABLE `lecturer_lec`
  ADD KEY `staff_id` (`staff_id`),
  ADD KEY `lec_id` (`lec_id`);

--
-- Indexes for table `lec_hall`
--
ALTER TABLE `lec_hall`
  ADD PRIMARY KEY (`hall_id`);

--
-- Indexes for table `postgraduate`
--
ALTER TABLE `postgraduate`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `practicle`
--
ALTER TABLE `practicle`
  ADD PRIMARY KEY (`practicle_id`),
  ADD KEY `practicle_ibfk_2` (`lab`),
  ADD KEY `practicle_ibfk_3` (`subject_code`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`student_id`),
  ADD KEY `fac_id` (`fac_id`),
  ADD KEY `course_id` (`course_id`);

--
-- Indexes for table `student_subject`
--
ALTER TABLE `student_subject`
  ADD PRIMARY KEY (`student_id`,`subject_code`),
  ADD KEY `subject_code` (`subject_code`);

--
-- Indexes for table `student_test`
--
ALTER TABLE `student_test`
  ADD PRIMARY KEY (`student_id`,`test_id`),
  ADD KEY `test_id` (`test_id`);

--
-- Indexes for table `subject`
--
ALTER TABLE `subject`
  ADD PRIMARY KEY (`subject_code`),
  ADD KEY `course_id` (`course_id`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`test_id`),
  ADD KEY `subject_code` (`subject_code`);

--
-- Indexes for table `undergraduate`
--
ALTER TABLE `undergraduate`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `course`
--
ALTER TABLE `course`
  MODIFY `course_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `faculty`
--
ALTER TABLE `faculty`
  MODIFY `faculty_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `instructor`
--
ALTER TABLE `instructor`
  MODIFY `instructor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `lab`
--
ALTER TABLE `lab`
  MODIFY `lab_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `lecture`
--
ALTER TABLE `lecture`
  MODIFY `lecture_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `lecturer`
--
ALTER TABLE `lecturer`
  MODIFY `lecturer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `lec_hall`
--
ALTER TABLE `lec_hall`
  MODIFY `hall_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `postgraduate`
--
ALTER TABLE `postgraduate`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `practicle`
--
ALTER TABLE `practicle`
  MODIFY `practicle_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `test_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `undergraduate`
--
ALTER TABLE `undergraduate`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `al_result`
--
ALTER TABLE `al_result`
  ADD CONSTRAINT `al_result_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `undergraduate` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `course`
--
ALTER TABLE `course`
  ADD CONSTRAINT `course_ibfk_1` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`faculty_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `instructor_practicle`
--
ALTER TABLE `instructor_practicle`
  ADD CONSTRAINT `instructor_practicle_ibfk_1` FOREIGN KEY (`practicle_id`) REFERENCES `practicle` (`practicle_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `instructor_practicle_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `instructor` (`instructor_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `lecture`
--
ALTER TABLE `lecture`
  ADD CONSTRAINT `lecture_ibfk_1` FOREIGN KEY (`hall_id`) REFERENCES `lec_hall` (`hall_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `lecture_ibfk_2` FOREIGN KEY (`subject_code`) REFERENCES `subject` (`subject_code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `lecturer`
--
ALTER TABLE `lecturer`
  ADD CONSTRAINT `lecturer_ibfk_1` FOREIGN KEY (`lecturer_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `lecturer_lec`
--
ALTER TABLE `lecturer_lec`
  ADD CONSTRAINT `lecturer_lec_ibfk_1` FOREIGN KEY (`lec_id`) REFERENCES `lecture` (`lecture_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `lecturer_lec_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `lecturer` (`lecturer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `postgraduate`
--
ALTER TABLE `postgraduate`
  ADD CONSTRAINT `postgraduate_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `practicle`
--
ALTER TABLE `practicle`
  ADD CONSTRAINT `practicle_ibfk_2` FOREIGN KEY (`lab`) REFERENCES `lab` (`lab_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `practicle_ibfk_3` FOREIGN KEY (`subject_code`) REFERENCES `subject` (`subject_code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_5` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `student_ibfk_6` FOREIGN KEY (`fac_id`) REFERENCES `faculty` (`faculty_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `student_ibfk_7` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `student_subject`
--
ALTER TABLE `student_subject`
  ADD CONSTRAINT `student_subject_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `student_subject_ibfk_2` FOREIGN KEY (`subject_code`) REFERENCES `subject` (`subject_code`);

--
-- Constraints for table `student_test`
--
ALTER TABLE `student_test`
  ADD CONSTRAINT `student_test_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `student_test_ibfk_2` FOREIGN KEY (`test_id`) REFERENCES `test` (`test_id`);

--
-- Constraints for table `subject`
--
ALTER TABLE `subject`
  ADD CONSTRAINT `subject_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`);

--
-- Constraints for table `test`
--
ALTER TABLE `test`
  ADD CONSTRAINT `test_ibfk_1` FOREIGN KEY (`subject_code`) REFERENCES `subject` (`subject_code`);

--
-- Constraints for table `undergraduate`
--
ALTER TABLE `undergraduate`
  ADD CONSTRAINT `undergraduate_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
