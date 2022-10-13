-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 18, 2022 at 09:13 PM
-- Server version: 10.4.20-MariaDB
-- PHP Version: 8.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `erp_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `business_data`
--

CREATE TABLE `business_data` (
  `one` text NOT NULL,
  `two` text NOT NULL,
  `three` text NOT NULL,
  `four` text NOT NULL,
  `five` text NOT NULL,
  `six` text NOT NULL,
  `seven` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `business_data`
--

INSERT INTO `business_data` (`one`, `two`, `three`, `four`, `five`, `six`, `seven`) VALUES
('KLERIDO', 'DOKIMASTIKO', 'T.A.X.: 987456321 - D.O.Y.: A PEIRAIAS', 'LAKWNIAS 38, PERAMA - T.K.: 18639', 'PHONE: 6999999999', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` int(128) NOT NULL,
  `brandname` varchar(128) NOT NULL,
  `profession` varchar(128) NOT NULL,
  `taxcode` varchar(9) NOT NULL,
  `address` varchar(128) NOT NULL,
  `area` varchar(128) NOT NULL,
  `zipcode` varchar(25) NOT NULL,
  `DOY` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone1` varchar(20) NOT NULL,
  `phone2` varchar(20) NOT NULL,
  `faxnumber` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `website` varchar(100) NOT NULL,
  `notes` longtext NOT NULL,
  `vatregime_id` int(11) NOT NULL,
  `payingway_id` int(11) NOT NULL,
  `pricelist_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `check_taxcode` int(2) NOT NULL,
  `check_doy` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `brandname`, `profession`, `taxcode`, `address`, `area`, `zipcode`, `DOY`, `surname`, `name`, `phone1`, `phone2`, `faxnumber`, `email`, `website`, `notes`, `vatregime_id`, `payingway_id`, `pricelist_id`, `user_id`, `check_taxcode`, `check_doy`) VALUES
(1, 'NAKAKIS KAI SIA OE', 'EMPORIO', '111111111', 'ETHINIKIS ANTISTASEWS 128A', 'AG DIMITRIOS', '18856', 'HLIOYPOLIS', 'NAKAKHS', 'GIORGOS', '698657854', '210518518', '', 'biciklerido@gmail.com', '', '', 1, 1, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `customer_invoices`
--

CREATE TABLE `customer_invoices` (
  `id` int(11) NOT NULL,
  `invoice_number` varchar(11) NOT NULL,
  `date` text NOT NULL,
  `time` text NOT NULL,
  `initial_value` double NOT NULL,
  `discount_percent` double NOT NULL,
  `discount_value` double NOT NULL,
  `value_beforevat` double NOT NULL,
  `vat_value` double NOT NULL,
  `quantity` double NOT NULL,
  `total` double NOT NULL,
  `purpose_of_tracking` text NOT NULL,
  `from_place` text NOT NULL,
  `to_place` text NOT NULL,
  `license_plate` varchar(15) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `invoicetype_id` int(11) NOT NULL,
  `payingway_id` int(11) NOT NULL,
  `vatregime_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `handwrited_invoice` int(2) NOT NULL,
  `remarks` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoicenumbering_1`
--

CREATE TABLE `invoicenumbering_1` (
  `number` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoicenumbering_2`
--

CREATE TABLE `invoicenumbering_2` (
  `number` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoicenumbering_3`
--

CREATE TABLE `invoicenumbering_3` (
  `number` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoicenumbering_4`
--

CREATE TABLE `invoicenumbering_4` (
  `number` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoicenumbering_5`
--

CREATE TABLE `invoicenumbering_5` (
  `number` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `id` int(11) NOT NULL,
  `code` varchar(125) NOT NULL,
  `description` varchar(125) NOT NULL,
  `barcode` varchar(125) NOT NULL,
  `purchase_price` double NOT NULL,
  `wholesale_price` double NOT NULL,
  `retail_price` double NOT NULL,
  `discount` double NOT NULL,
  `remaining` double NOT NULL DEFAULT 0,
  `supplierID` int(11) NOT NULL,
  `vat_categorieID` int(11) NOT NULL,
  `measurement_unitID` int(11) NOT NULL,
  `retail_contains_vat` int(2) NOT NULL,
  `wholesale_contains_vat` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`id`, `code`, `description`, `barcode`, `purchase_price`, `wholesale_price`, `retail_price`, `discount`, `remaining`, `supplierID`, `vat_categorieID`, `measurement_unitID`, `retail_contains_vat`, `wholesale_contains_vat`) VALUES
(1, '0001', 'ITEM 1', '', 0, 12, 15, 0, 0, 0, 3, 2, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `item_transactions1`
--

CREATE TABLE `item_transactions1` (
  `id` int(255) NOT NULL,
  `date` tinytext NOT NULL,
  `time` tinytext NOT NULL,
  `item_code` varchar(125) NOT NULL,
  `item_description` varchar(125) NOT NULL,
  `quantity` double NOT NULL,
  `unit_price` double NOT NULL,
  `discount` double NOT NULL,
  `vat` double NOT NULL,
  `etiology` text NOT NULL,
  `total` double NOT NULL,
  `transaction_code` int(11) NOT NULL,
  `customer_invoiceID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `measurement_unit`
--

CREATE TABLE `measurement_unit` (
  `id` int(11) NOT NULL,
  `code` varchar(3) NOT NULL,
  `description` varchar(125) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `measurement_unit`
--

INSERT INTO `measurement_unit` (`id`, `code`, `description`) VALUES
(1, 'KIL', 'KILOS'),
(2, 'PIE', 'PIECE'),
(3, 'CME', 'CUBIC METERS'),
(4, 'MET', 'METERS'),
(5, 'SQM', 'SQUARE METERS');

-- --------------------------------------------------------

--
-- Table structure for table `paying_way`
--

CREATE TABLE `paying_way` (
  `id` int(11) NOT NULL,
  `description` varchar(125) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `paying_way`
--

INSERT INTO `paying_way` (`id`, `description`) VALUES
(1, 'Cash'),
(2, 'On account'),
(3, 'Bank deposit'),
(4, 'Credit card');

-- --------------------------------------------------------

--
-- Table structure for table `pricelist`
--

CREATE TABLE `pricelist` (
  `id` int(11) NOT NULL,
  `description` varchar(125) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pricelist`
--

INSERT INTO `pricelist` (`id`, `description`) VALUES
(1, 'Wholesale'),
(2, 'Retail');

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `supplier_id` int(128) NOT NULL,
  `brandname` varchar(128) NOT NULL,
  `profession` varchar(128) NOT NULL,
  `taxcode` varchar(9) NOT NULL,
  `address` varchar(128) NOT NULL,
  `area` varchar(128) NOT NULL,
  `zipcode` varchar(25) NOT NULL,
  `DOY` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone1` varchar(20) NOT NULL,
  `phone2` varchar(20) NOT NULL,
  `faxnumber` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `website` varchar(100) NOT NULL,
  `notes` longtext NOT NULL,
  `vatregime_id` int(11) NOT NULL,
  `payingway_id` int(11) NOT NULL,
  `pricelist_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `check_taxcode` int(2) NOT NULL,
  `check_doy` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`supplier_id`, `brandname`, `profession`, `taxcode`, `address`, `area`, `zipcode`, `DOY`, `surname`, `name`, `phone1`, `phone2`, `faxnumber`, `email`, `website`, `notes`, `vatregime_id`, `payingway_id`, `pricelist_id`, `user_id`, `check_taxcode`, `check_doy`) VALUES
(0, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 2, 2, 2, 1, 0, 0),
(1, 'PROMITHEFTIS 1', 'AAAA', '111111111', '', '', '', 'A PEIRAIAS', '', '', '', '', '', '', '', '', 1, 2, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `supplier_invoices`
--

CREATE TABLE `supplier_invoices` (
  `id` int(11) NOT NULL,
  `invoice_number` varchar(11) NOT NULL,
  `date` text NOT NULL,
  `time` text NOT NULL,
  `initial_value` double NOT NULL,
  `discount_percent` double NOT NULL,
  `discount_value` double NOT NULL,
  `value_beforevat` double NOT NULL,
  `vat_value` double NOT NULL,
  `quantity` double NOT NULL,
  `total` double NOT NULL,
  `purpose_of_tracking` text NOT NULL,
  `from_place` text NOT NULL,
  `to_place` text NOT NULL,
  `license_plate` varchar(15) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `invoicetype_id` int(11) NOT NULL,
  `payment_id` int(11) NOT NULL,
  `vatregime_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `remarks` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `type_of_customer_document`
--

CREATE TABLE `type_of_customer_document` (
  `id` int(11) NOT NULL,
  `abbreviation` varchar(5) NOT NULL,
  `description` varchar(125) NOT NULL,
  `delivery` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `type_of_customer_document`
--

INSERT INTO `type_of_customer_document` (`id`, `abbreviation`, `description`, `delivery`) VALUES
(1, 'SI-DN', 'SALES INVOICE - DELIVERY NOTE', 1),
(2, 'SERIN', 'SERVICE INVOICE', 0),
(3, 'RRT', 'RECEIPT OF RETAIL TRANSACTIONS', 1),
(4, 'SOS', 'RECEIPT OF SERVICE', 0),
(5, 'SPCA', 'SPECIAL CANCELLATION', 0);

-- --------------------------------------------------------

--
-- Table structure for table `type_of_supplier_document`
--

CREATE TABLE `type_of_supplier_document` (
  `id` int(11) NOT NULL,
  `abbreviation` varchar(5) NOT NULL,
  `description` varchar(125) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `type_of_supplier_document`
--

INSERT INTO `type_of_supplier_document` (`id`, `abbreviation`, `description`) VALUES
(1, 'PI', 'PURCHASE INVOICE'),
(2, 'ISERV', 'PURCHASE INVOICE (for services)');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name_surname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `name_surname`) VALUES
(1, 'admin', 'admin', 'General User');

-- --------------------------------------------------------

--
-- Table structure for table `vat_categorie`
--

CREATE TABLE `vat_categorie` (
  `id` int(11) NOT NULL,
  `description` varchar(125) NOT NULL,
  `vat_percentage` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `vat_categorie`
--

INSERT INTO `vat_categorie` (`id`, `description`, `vat_percentage`) VALUES
(1, 'Low (6%)', 6),
(2, 'Medium (13%)', 13),
(3, 'High (24%)', 24),
(4, 'Zero V.A.T.', 0),
(5, 'Medium Low (9%)', 9);

-- --------------------------------------------------------

--
-- Table structure for table `vat_regime`
--

CREATE TABLE `vat_regime` (
  `id` int(25) NOT NULL,
  `description` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `vat_regime`
--

INSERT INTO `vat_regime` (`id`, `description`) VALUES
(1, 'Normal VAT regime'),
(2, 'Zero VAT regime'),
(3, 'Reduced VAT (Border)');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`),
  ADD KEY `vatregime_id` (`vatregime_id`),
  ADD KEY `payingway_id` (`payingway_id`),
  ADD KEY `pricelist_id` (`pricelist_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `customer_invoices`
--
ALTER TABLE `customer_invoices`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `payingway_id` (`payingway_id`),
  ADD KEY `vatregime_id` (`vatregime_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `invoicetype_id` (`invoicetype_id`);

--
-- Indexes for table `invoicenumbering_1`
--
ALTER TABLE `invoicenumbering_1`
  ADD PRIMARY KEY (`number`);

--
-- Indexes for table `invoicenumbering_2`
--
ALTER TABLE `invoicenumbering_2`
  ADD PRIMARY KEY (`number`);

--
-- Indexes for table `invoicenumbering_3`
--
ALTER TABLE `invoicenumbering_3`
  ADD PRIMARY KEY (`number`);

--
-- Indexes for table `invoicenumbering_4`
--
ALTER TABLE `invoicenumbering_4`
  ADD PRIMARY KEY (`number`);

--
-- Indexes for table `invoicenumbering_5`
--
ALTER TABLE `invoicenumbering_5`
  ADD PRIMARY KEY (`number`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `code` (`code`),
  ADD KEY `measurement_unitID` (`measurement_unitID`),
  ADD KEY `vat_categorieID` (`vat_categorieID`),
  ADD KEY `supplierID` (`supplierID`);

--
-- Indexes for table `item_transactions1`
--
ALTER TABLE `item_transactions1`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `measurement_unit`
--
ALTER TABLE `measurement_unit`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `paying_way`
--
ALTER TABLE `paying_way`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pricelist`
--
ALTER TABLE `pricelist`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`supplier_id`),
  ADD KEY `payingway_id` (`payingway_id`),
  ADD KEY `vatregime_id` (`vatregime_id`),
  ADD KEY `pricelist_id` (`pricelist_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `supplier_invoices`
--
ALTER TABLE `supplier_invoices`
  ADD PRIMARY KEY (`id`),
  ADD KEY `invoicetype_id` (`invoicetype_id`),
  ADD KEY `payment_id` (`payment_id`),
  ADD KEY `supplier_id` (`supplier_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `vatregime_id` (`vatregime_id`);

--
-- Indexes for table `type_of_customer_document`
--
ALTER TABLE `type_of_customer_document`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `type_of_supplier_document`
--
ALTER TABLE `type_of_supplier_document`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vat_categorie`
--
ALTER TABLE `vat_categorie`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vat_regime`
--
ALTER TABLE `vat_regime`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int(128) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `customer_invoices`
--
ALTER TABLE `customer_invoices`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoicenumbering_1`
--
ALTER TABLE `invoicenumbering_1`
  MODIFY `number` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoicenumbering_2`
--
ALTER TABLE `invoicenumbering_2`
  MODIFY `number` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoicenumbering_3`
--
ALTER TABLE `invoicenumbering_3`
  MODIFY `number` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoicenumbering_4`
--
ALTER TABLE `invoicenumbering_4`
  MODIFY `number` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoicenumbering_5`
--
ALTER TABLE `invoicenumbering_5`
  MODIFY `number` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `item_transactions1`
--
ALTER TABLE `item_transactions1`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `measurement_unit`
--
ALTER TABLE `measurement_unit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `paying_way`
--
ALTER TABLE `paying_way`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pricelist`
--
ALTER TABLE `pricelist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `supplier`
--
ALTER TABLE `supplier`
  MODIFY `supplier_id` int(128) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `supplier_invoices`
--
ALTER TABLE `supplier_invoices`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `type_of_customer_document`
--
ALTER TABLE `type_of_customer_document`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `type_of_supplier_document`
--
ALTER TABLE `type_of_supplier_document`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `vat_categorie`
--
ALTER TABLE `vat_categorie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `vat_regime`
--
ALTER TABLE `vat_regime`
  MODIFY `id` int(25) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_2` FOREIGN KEY (`vatregime_id`) REFERENCES `vat_regime` (`id`),
  ADD CONSTRAINT `customer_ibfk_3` FOREIGN KEY (`payingway_id`) REFERENCES `paying_way` (`id`),
  ADD CONSTRAINT `customer_ibfk_4` FOREIGN KEY (`pricelist_id`) REFERENCES `pricelist` (`id`),
  ADD CONSTRAINT `customer_ibfk_5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `customer_invoices`
--
ALTER TABLE `customer_invoices`
  ADD CONSTRAINT `customer_invoices_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `customer_invoices_ibfk_2` FOREIGN KEY (`payingway_id`) REFERENCES `paying_way` (`id`),
  ADD CONSTRAINT `customer_invoices_ibfk_3` FOREIGN KEY (`vatregime_id`) REFERENCES `vat_regime` (`id`),
  ADD CONSTRAINT `customer_invoices_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `customer_invoices_ibfk_5` FOREIGN KEY (`invoicetype_id`) REFERENCES `type_of_customer_document` (`id`);

--
-- Constraints for table `items`
--
ALTER TABLE `items`
  ADD CONSTRAINT `items_ibfk_2` FOREIGN KEY (`measurement_unitID`) REFERENCES `measurement_unit` (`id`),
  ADD CONSTRAINT `items_ibfk_3` FOREIGN KEY (`vat_categorieID`) REFERENCES `vat_categorie` (`id`),
  ADD CONSTRAINT `items_ibfk_4` FOREIGN KEY (`supplierID`) REFERENCES `supplier` (`supplier_id`);

--
-- Constraints for table `supplier`
--
ALTER TABLE `supplier`
  ADD CONSTRAINT `supplier_ibfk_1` FOREIGN KEY (`payingway_id`) REFERENCES `paying_way` (`id`),
  ADD CONSTRAINT `supplier_ibfk_2` FOREIGN KEY (`vatregime_id`) REFERENCES `vat_regime` (`id`),
  ADD CONSTRAINT `supplier_ibfk_3` FOREIGN KEY (`pricelist_id`) REFERENCES `pricelist` (`id`),
  ADD CONSTRAINT `supplier_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `supplier_invoices`
--
ALTER TABLE `supplier_invoices`
  ADD CONSTRAINT `supplier_invoices_ibfk_1` FOREIGN KEY (`invoicetype_id`) REFERENCES `type_of_supplier_document` (`id`),
  ADD CONSTRAINT `supplier_invoices_ibfk_2` FOREIGN KEY (`payment_id`) REFERENCES `paying_way` (`id`),
  ADD CONSTRAINT `supplier_invoices_ibfk_3` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`),
  ADD CONSTRAINT `supplier_invoices_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `supplier_invoices_ibfk_5` FOREIGN KEY (`vatregime_id`) REFERENCES `vat_regime` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
