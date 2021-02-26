-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Tempo de geração: 26-Fev-2021 às 20:57
-- Versão do servidor: 5.7.31
-- versão do PHP: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `testern`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `tabela_info`
--

DROP TABLE IF EXISTS `tabela_info`;
CREATE TABLE IF NOT EXISTS `tabela_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ID_tabela` int(11) NOT NULL,
  `Nome` varchar(200) NOT NULL,
  `Sexo` char(1) NOT NULL,
  `Data_Nascimento` date NOT NULL,
  `Nota_1Trimestre` int(11) NOT NULL,
  `Nota_2Trimestre` int(11) NOT NULL,
  `Nota_3Trimestre` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=757 DEFAULT CHARSET=latin1;

