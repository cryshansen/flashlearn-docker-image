-- phpMyAdmin SQL Dump
-- version 4.9.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Oct 10, 2025 at 05:13 PM
-- Server version: 5.7.26
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `flashlearn`
--

-- --------------------------------------------------------

--
-- Table structure for table `flashcard`
--

CREATE TABLE `flashcard` (
  `id` bigint(20) NOT NULL,
  `answer` varchar(2000) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `difficulty` varchar(255) DEFAULT NULL,
  `question` varchar(255) DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `answer_audio_path` varchar(255) DEFAULT NULL,
  `question_audio_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `flashcard`
--

INSERT INTO `flashcard` (`id`, `answer`, `color`, `difficulty`, `question`, `topic`, `session_id`, `answer_audio_path`, `question_audio_path`) VALUES
(1, 'The primary pigment is <strong>chlorophyll</strong>, which <u>absorbs</u> light energy.', 'success', 'easy', 'What is the primary pigment involved in <strong>photosynthesis</strong>?', 'Photosynthesis', 3, 'audio/1/1_answer.mp3', 'audio/1/1_question.mp3'),
(2, '<strong>Photosynthesis</strong> mainly occurs in the <strong>chloroplast</strong> of plant cells.', 'success', 'easy', 'In which organelle does <strong>photosynthesis</strong> mainly occur?', 'Photosynthesis', 3, 'audio/1/2_answer.mp3', 'audio/1/2_question.mp3'),
(3, 'The two main stages are the <strong>light-dependent reactions</strong> and <strong>Calvin cycle</strong>.', 'warning', 'medium', 'What are the two main stages of <strong>photosynthesis</strong>?', 'Photosynthesis', 3, 'audio/1/3_answer.mp3', 'audio/1/3_question.mp3'),
(4, 'The <strong>light-dependent reactions</strong> <u>convert</u> light energy into <strong>ATP</strong> and <strong>NADPH</strong>.', 'warning', 'medium', 'What does the <strong>light-dependent reactions</strong> convert light energy into?', 'Photosynthesis', 3, 'audio/1/4_answer.mp3', 'audio/1/4_question.mp3'),
(5, 'The gas <strong>carbon dioxide (CO2)</strong> is <u>used</u> in the <strong>Calvin cycle</strong>.', 'success', 'easy', 'What gas is <u>used</u> in the <strong>Calvin cycle</strong>?', 'Photosynthesis', 3, 'audio/1/5_answer.mp3', 'audio/1/5_question.mp3'),
(6, '<em>The overall equation is 6CO2 + 6H2O + light energy → C6H12O6 + 6O2.</em>', 'danger', 'hard', 'What is the overall equation for <strong>photosynthesis</strong>?', 'Photosynthesis', 3, 'audio/1/6_answer.mp3', 'audio/1/6_question.mp3'),
(7, 'Red and blue wavelengths are most effective for <strong>photosynthesis</strong>.', 'warning', 'medium', 'Which wavelengths of light are most effective for <strong>photosynthesis</strong>?', 'Photosynthesis', 3, 'audio/1/7_answer.mp3', 'audio/1/7_question.mp3'),
(8, 'The end products are <strong>glucose</strong>, different types of sugars, and other carbohydrates.', 'warning', 'medium', 'What are the end products of the <strong>Calvin cycle</strong>?', 'Photosynthesis', 3, 'audio/1/8_answer.mp3', 'audio/1/8_question.mp3'),
(9, '<strong>Water</strong> is split to <u>provide</u> electrons and protons in the <strong>light reactions</strong>.', 'danger', 'hard', 'What role does <strong>water</strong> play in <strong>photosynthesis</strong>?', 'Photosynthesis', 3, 'audio/1/9_answer.mp3', 'audio/1/9_question.mp3'),
(10, '<strong>Photosynthesis</strong> <u>produces</u> oxygen and <u>stores</u> energy in glucose for other organisms.', 'warning', 'medium', 'How does <strong>photosynthesis</strong> benefit the ecosystem?', 'Photosynthesis', 3, 'audio/1/10_answer.mp3', 'audio/1/10_question.mp3'),
(11, 'It <u>automates</u> the <u>deployment</u>, <u>scaling</u>, and <u>management</u> of containerized applications.', 'success', 'easy', 'What is the primary purpose of <strong>Kubernetes</strong>?', 'Kubernetes', 4, 'http://localhost:8080/audio/1/11_answer.mp3', 'http://localhost:8080/audio/1/11_question.mp3'),
(12, 'A <strong>Pod</strong> is the smallest deployable unit that can <u>host</u> one or more containers.', 'success', 'easy', 'What does <strong>Pod</strong> represent in Kubernetes?', 'Kubernetes', 4, 'audio/1/12_answer.mp3', 'audio/1/12_question.mp3'),
(13, 'It <u>manages</u> containerized applications across a cluster to provide <em>high availability</em> and <em>scalability</em>.', 'warning', 'medium', 'What <strong>container orchestration</strong> capabilities does Kubernetes provide?', 'Kubernetes', 4, 'audio/1/13_answer.mp3', 'audio/1/13_question.mp3'),
(14, 'A <strong>Service</strong> is an abstraction that <u>defines</u> a logical set of Pods and a policy for <u>accessing</u> them.', 'warning', 'medium', 'What is a <strong>Service</strong> in Kubernetes?', 'Kubernetes', 4, 'audio/1/14_answer.mp3', 'audio/1/14_question.mp3'),
(15, 'A <strong>Node</strong> is a worker machine that <u>executes</u> Pods and is managed by the <strong>Master node</strong>.', 'success', 'easy', 'What role does a <strong>Node</strong> play in a Kubernetes cluster?', 'Kubernetes', 4, 'audio/1/15_answer.mp3', 'audio/1/15_question.mp3'),
(16, 'A <strong>Deployment</strong> provides declarative updates for Pods and <u>ensures</u> the desired number of replicas are <u>running</u>.', 'warning', 'medium', 'What is a <strong>Deployment</strong> in Kubernetes?', 'Kubernetes', 4, 'audio/1/16_answer.mp3', 'audio/1/16_question.mp3'),
(17, 'The <strong>Kubelet</strong> is an agent that <u>runs</u> on each Node and <u>ensures</u> the defined containers are <u>running</u>.', 'warning', 'medium', 'What does the term <strong>Kubelet</strong> refer to?', 'Kubernetes', 4, 'audio/1/17_answer.mp3', 'audio/1/17_question.mp3'),
(18, 'A <strong>ConfigMap</strong> is used to <u>store</u> non-sensitive configuration data in key-value pairs to <u>inject</u> into Pods.', 'warning', 'medium', 'What is the purpose of a <strong>ConfigMap</strong>?', 'Kubernetes', 4, 'audio/1/18_answer.mp3', 'audio/1/18_question.mp3'),
(19, 'The <strong>Kubernetes Scheduler</strong> <u>assigns</u> Pods to Nodes based on resource availability and other constraints.', 'danger', 'hard', 'What feature does the <strong>Kubernetes Scheduler</strong> provide?', 'Kubernetes', 4, 'audio/1/19_answer.mp3', 'audio/1/19_question.mp3'),
(20, '<strong>Horizontal Pod Autoscaler</strong> automatically <u>scales</u> the number of Pods based on observed CPU utilization or other select metrics.', 'warning', 'medium', 'What does <strong>Horizontal Pod Autoscaler</strong> do?', 'Kubernetes', 4, 'audio/1/20_answer.mp3', 'audio/1/20_question.mp3'),
(21, 'A <strong>Namespace</strong> provides a mechanism to <u>divide</u> cluster resources between multiple users or applications.', 'warning', 'medium', 'What is a <strong>Namespace</strong> in Kubernetes?', 'Kubernetes', 4, 'audio/1/21_answer.mp3', 'audio/1/21_question.mp3'),
(22, 'The <strong>Master Node</strong> is responsible for <u>managing</u> the Kubernetes cluster and coordinating the activities of Workers.', 'danger', 'hard', 'What is the role of the <strong>Master Node</strong>?', 'Kubernetes', 4, 'audio/1/22_answer.mp3', 'audio/1/22_question.mp3'),
(23, '<strong>etcd</strong> is a distributed key-value store that <u>stores</u> all cluster data, including configurations and states.', 'danger', 'hard', 'What does <strong>etcd</strong> do in Kubernetes?', 'Kubernetes', 4, 'audio/1/23_answer.mp3', 'audio/1/23_question.mp3'),
(24, 'A <strong>Volume</strong> is a piece of storage in the Kubernetes cluster that <u>exists</u> beyond the lifespan of a Pod.', 'warning', 'medium', 'What is a <strong>Volume</strong> in Kubernetes?', 'Kubernetes', 4, 'audio/1/24_answer.mp3', 'audio/1/24_question.mp3'),
(25, '<strong>ClusterIP</strong> makes a service accessible only within the cluster, while <strong>NodePort</strong> exposes it on a port on each Node.', 'danger', 'hard', 'What is the difference between a <strong>ClusterIP</strong> and a <strong>NodePort</strong> Service?', 'Kubernetes', 4, 'audio/1/25_answer.mp3', 'audio/1/25_question.mp3'),
(26, '<strong>CRDs</strong> allow users to <u>extend</u> Kubernetes capabilities by defining their own resource types.', 'danger', 'hard', 'What is the purpose of <strong>Custom Resource Definitions (CRDs)</strong>?', 'Kubernetes', 4, 'audio/1/26_answer.mp3', 'audio/1/26_question.mp3'),
(27, '<strong>kubectl</strong> is used to <u>interact</u> with the Kubernetes API and manage cluster resources.', 'success', 'easy', 'What is the <strong>kubectl</strong> command line tool used for?', 'Kubernetes', 4, 'audio/1/27_answer.mp3', 'audio/1/27_question.mp3');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `flashcard`
--
ALTER TABLE `flashcard`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKexair5xuhipounj3ucnnulnrm` (`session_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `flashcard`
--
ALTER TABLE `flashcard`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `flashcard`
--
ALTER TABLE `flashcard`
  ADD CONSTRAINT `FKexair5xuhipounj3ucnnulnrm` FOREIGN KEY (`session_id`) REFERENCES `study_session` (`id`);
