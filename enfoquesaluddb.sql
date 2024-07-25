-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 19-11-2023 a las 01:30:20
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4
SET
  SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

START TRANSACTION;

SET
  time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */
;

/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */
;

/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */
;

/*!40101 SET NAMES utf8mb4 */
;

--
-- Base de datos: `enfoquesaluddb`
--
-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `appointment`
--
CREATE TABLE `appointment` (
  `Id` int(11) NOT NULL,
  `Id_patient` int(11) NOT NULL,
  `Id_doctor` int(11) NOT NULL,
  `hour_start` time NOT NULL,
  `hour_finale` time NOT NULL,
  `day` date NOT NULL,
  `state` int(11) NOT NULL DEFAULT 1,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `availability`
--
CREATE TABLE `availability` (
  `Id` int(11) NOT NULL,
  `Id_doctor` int(11) NOT NULL,
  `day` date NOT NULL,
  `hour_start` time NOT NULL,
  `hour_finale` time NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `clinic_location`
--
CREATE TABLE `clinic_location` (
  `Id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `postal_code` varchar(20) DEFAULT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `doctor`
--
CREATE TABLE `doctor` (
  `Id` int(11) NOT NULL,
  `Id_user` int(11) NOT NULL,
  `speciality` varchar(60) NOT NULL,
  `medical_license_number` varchar(50) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `invoice`
--
CREATE TABLE `invoice` (
  `Id` int(11) NOT NULL,
  `Id_patient` int(11) NOT NULL,
  `invoice_number` varchar(20) DEFAULT NULL,
  `invoice_date` date DEFAULT NULL,
  `amount` decimal(10, 2) DEFAULT NULL,
  `paid` tinyint(1) DEFAULT 0,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `module`
--
CREATE TABLE `module` (
  `Id` int(11) NOT NULL,
  `description` varchar(50) NOT NULL,
  `url` varchar(30) DEFAULT NULL,
  `icon` varchar(200) DEFAULT NULL,
  `submodule` int(11) DEFAULT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `patient`
--
CREATE TABLE `patient` (
  `Id` int(11) NOT NULL,
  `Id_user` int(11) NOT NULL,
  `size` decimal(10, 2) NOT NULL,
  `weight` decimal(10, 2) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `payment`
--
CREATE TABLE `payment` (
  `Id` int(11) NOT NULL,
  `Id_invoice` int(11) NOT NULL,
  `payment_date` date DEFAULT NULL,
  `amount` decimal(10, 2) DEFAULT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `permission`
--
CREATE TABLE `permission` (
  `Id` int(11) NOT NULL,
  `Id_module` int(11) NOT NULL,
  `Id_role` int(11) NOT NULL,
  `c` tinyint(4) NOT NULL,
  `r` tinyint(4) NOT NULL,
  `u` tinyint(4) NOT NULL,
  `d` tinyint(4) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `person`
--
CREATE TABLE `person` (
  `Id` int(11) NOT NULL,
  `name` varchar(40) NOT NULL,
  `lastname` varchar(80) NOT NULL,
  `document` tinyint(4) NOT NULL,
  `n_document` varchar(20) NOT NULL,
  `gender` tinyint(4) NOT NULL,
  `born_date` date NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `record`
--
CREATE TABLE `record` (
  `Id` int(11) NOT NULL,
  `Id_appointment` int(11) NOT NULL,
  `problem` text NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `role`
--
CREATE TABLE `role` (
  `Id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `state` int(11) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------------------------------
INSERT INTO
  `role` (`id`,`name`, `image`)
VALUES
  ('1','Admin', 'admin.png'),
  ('2','User', 'user.png'),
  ('3','Manager', 'manager.png');

--
-- Estructura de tabla para la tabla `user`
--
CREATE TABLE `user` (
  `Id` int(11) NOT NULL,
  `Id_person` int(11) NOT NULL,
  `Id_role` int(11) NOT NULL,
  `Id_clinic` int(11) DEFAULT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(200) NOT NULL,
  `avatar` varchar(255) NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `last_login` timestamp NOT NULL DEFAULT current_timestamp(),
  `state` int(11) NOT NULL DEFAULT 1,
  `login_attempts` int(11) DEFAULT 0
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci;

--
-- Índices para tablas volcadas
--
--
-- Indices de la tabla `appointment`
--
ALTER TABLE
  `appointment`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Id_patient` (`Id_patient`),
ADD
  KEY `FK_Doctor` (`Id_doctor`);

--
-- Indices de la tabla `availability`
--
ALTER TABLE
  `availability`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Id_doctor` (`Id_doctor`);

--
-- Indices de la tabla `clinic_location`
--
ALTER TABLE
  `clinic_location`
ADD
  PRIMARY KEY (`Id`);

--
-- Indices de la tabla `doctor`
--
ALTER TABLE
  `doctor`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Id_user` (`Id_user`);

--
-- Indices de la tabla `invoice`
--
ALTER TABLE
  `invoice`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_patient` (`Id_patient`);

--
-- Indices de la tabla `module`
--
ALTER TABLE
  `module`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Submodule` (`submodule`);

--
-- Indices de la tabla `patient`
--
ALTER TABLE
  `patient`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_user_patient` (`Id_user`);

--
-- Indices de la tabla `payment`
--
ALTER TABLE
  `payment`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Id_invoice` (`Id_invoice`);

--
-- Indices de la tabla `permission`
--
ALTER TABLE
  `permission`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Id_module` (`Id_module`),
ADD
  KEY `FK_Role` (`Id_role`);

--
-- Indices de la tabla `person`
--
ALTER TABLE
  `person`
ADD
  PRIMARY KEY (`Id`);

--
-- Indices de la tabla `record`
--
ALTER TABLE
  `record`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Id_appointment` (`Id_appointment`);

--
-- Indices de la tabla `role`
--
ALTER TABLE
  `role`
ADD
  PRIMARY KEY (`Id`);

--
-- Indices de la tabla `user`
--
ALTER TABLE
  `user`
ADD
  PRIMARY KEY (`Id`),
ADD
  KEY `FK_Id_person` (`Id_person`),
ADD
  KEY `FK_Id_Role` (`Id_role`),
ADD
  KEY `FK_Id_Clinic` (`Id_clinic`);

--
-- AUTO_INCREMENT de las tablas volcadas
--
--
-- AUTO_INCREMENT de la tabla `appointment`
--
ALTER TABLE
  `appointment`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `availability`
--
ALTER TABLE
  `availability`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clinic_location`
--
ALTER TABLE
  `clinic_location`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `doctor`
--
ALTER TABLE
  `doctor`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `invoice`
--
ALTER TABLE
  `invoice`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `module`
--
ALTER TABLE
  `module`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `patient`
--
ALTER TABLE
  `patient`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `payment`
--
ALTER TABLE
  `payment`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `permission`
--
ALTER TABLE
  `permission`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `person`
--
ALTER TABLE
  `person`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `record`
--
ALTER TABLE
  `record`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `role`
--
ALTER TABLE
  `role`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE
  `user`
MODIFY
  `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--
--
-- Filtros para la tabla `appointment`
--
ALTER TABLE
  `appointment`
ADD
  CONSTRAINT `FK_Doctor` FOREIGN KEY (`Id_doctor`) REFERENCES `doctor` (`Id`),
ADD
  CONSTRAINT `FK_Id_patient` FOREIGN KEY (`Id_patient`) REFERENCES `patient` (`Id`);

--
-- Filtros para la tabla `availability`
--
ALTER TABLE
  `availability`
ADD
  CONSTRAINT `FK_Id_doctor` FOREIGN KEY (`Id_doctor`) REFERENCES `doctor` (`Id`);

--
-- Filtros para la tabla `doctor`
--
ALTER TABLE
  `doctor`
ADD
  CONSTRAINT `FK_Id_user` FOREIGN KEY (`Id_user`) REFERENCES `user` (`Id`);

--
-- Filtros para la tabla `invoice`
--
ALTER TABLE
  `invoice`
ADD
  CONSTRAINT `FK_patient` FOREIGN KEY (`Id_patient`) REFERENCES `patient` (`Id`);

--
-- Filtros para la tabla `module`
--
ALTER TABLE
  `module`
ADD
  CONSTRAINT `FK_Submodule` FOREIGN KEY (`submodule`) REFERENCES `module` (`Id`);

--
-- Filtros para la tabla `patient`
--
ALTER TABLE
  `patient`
ADD
  CONSTRAINT `FK_user_patient` FOREIGN KEY (`Id_user`) REFERENCES `user` (`Id`);

--
-- Filtros para la tabla `payment`
--
ALTER TABLE
  `payment`
ADD
  CONSTRAINT `FK_Id_invoice` FOREIGN KEY (`Id_invoice`) REFERENCES `invoice` (`Id`);

--
-- Filtros para la tabla `permission`
--
ALTER TABLE
  `permission`
ADD
  CONSTRAINT `FK_Id_module` FOREIGN KEY (`Id_module`) REFERENCES `module` (`Id`),
ADD
  CONSTRAINT `FK_Role` FOREIGN KEY (`Id_role`) REFERENCES `role` (`Id`);

--
-- Filtros para la tabla `record`
--
ALTER TABLE
  `record`
ADD
  CONSTRAINT `FK_Id_appointment` FOREIGN KEY (`Id_appointment`) REFERENCES `appointment` (`Id`);

--
-- Filtros para la tabla `user`
--
ALTER TABLE
  `user`
ADD
  CONSTRAINT `FK_Id_Clinic` FOREIGN KEY (`Id_clinic`) REFERENCES `clinic_location` (`Id`),
ADD
  CONSTRAINT `FK_Id_Role` FOREIGN KEY (`Id_role`) REFERENCES `role` (`Id`),
ADD
  CONSTRAINT `FK_Id_person` FOREIGN KEY (`Id_person`) REFERENCES `person` (`Id`);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */
;

/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */
;

/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */
;