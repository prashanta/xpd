-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Aug 30, 2016 at 09:12 AM
-- Server version: 10.1.9-MariaDB
-- PHP Version: 5.5.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `radix`
--

-- --------------------------------------------------------

--
-- Table structure for table `ev_aprvvend`
--

CREATE TABLE `ev_aprvvend` (
  `id` int(10) UNSIGNED NOT NULL,
  `APVType` varchar(6) NOT NULL COMMENT '"MTL" Material Suppliers or "Sub" Subcontractors',
  `VendorNum` int(4) NOT NULL COMMENT 'Vendor Number',
  `PartNum` varchar(40) NOT NULL COMMENT 'The PartNum field identifies the Part and is used as the primary key.',
  `Class` varchar(8) NOT NULL COMMENT 'Inventory class. Only pertains when APVType = "MTL". The Class field can be blank or must be valid in the PartClass master file.',
  `CustNum` int(4) NOT NULL COMMENT 'The unique key  of the Parent Customer record for the ShipTo.',
  `OpCode` varchar(10) NOT NULL COMMENT 'Operation Master Code - Links record with a OpMaster record.    Only pertains when APVType = "Sub".',
  `ManPartNum` varchar(40) NOT NULL COMMENT 'The Manufacturer PartNum field identifies the Part.',
  `Manufacturer` varchar(50) NOT NULL COMMENT 'Name of the Manufacturer'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_jobasmbl`
--

CREATE TABLE `ev_jobasmbl` (
  `id` int(10) UNSIGNED NOT NULL,
  `JobNum` varchar(28) NOT NULL,
  `JobComplete` tinyint(1) NOT NULL,
  `AssemblySeq` int(4) NOT NULL,
  `PartNum` varchar(40) NOT NULL,
  `QtyPer` float NOT NULL,
  `RequiredQty` float NOT NULL,
  `IUM` varchar(4) NOT NULL,
  `Parent` int(4) NOT NULL,
  `StartDate` date NOT NULL,
  `DueDate` date NOT NULL,
  `PriorPeer` int(4) NOT NULL,
  `NextPeer` int(4) NOT NULL,
  `Child` int(4) NOT NULL,
  `BomSequence` int(4) NOT NULL,
  `BomLevel` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Epicor Vantage - Job Assembly file.';

-- --------------------------------------------------------

--
-- Table structure for table `ev_jobhead`
--

CREATE TABLE `ev_jobhead` (
  `id` int(10) UNSIGNED NOT NULL,
  `JobNum` varchar(28) NOT NULL,
  `PartNum` varchar(40) NOT NULL,
  `PartDescription` varchar(2000) NOT NULL,
  `ProdQty` float NOT NULL,
  `IUM` varchar(4) NOT NULL,
  `StartDate` date DEFAULT NULL,
  `ReqDueDate` date DEFAULT NULL,
  `JobClosed` tinyint(1) NOT NULL,
  `ClosedDate` date DEFAULT NULL,
  `JobComplete` tinyint(1) NOT NULL,
  `JobReleased` tinyint(1) NOT NULL,
  `JobCompletionDate` date DEFAULT NULL,
  `DueDate` date DEFAULT NULL,
  `ProjectID` varchar(40) NOT NULL,
  `JobFirm` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Epicor Vantage - Job Head file.';

-- --------------------------------------------------------

--
-- Table structure for table `ev_jobmtl`
--

CREATE TABLE `ev_jobmtl` (
  `id` int(10) UNSIGNED NOT NULL,
  `JobNum` varchar(28) NOT NULL,
  `JobComplete` tinyint(1) NOT NULL,
  `AssemblySeq` int(4) NOT NULL,
  `MtlSeq` int(4) NOT NULL,
  `BubbleNum` varchar(20) NOT NULL,
  `PartNum` varchar(40) NOT NULL,
  `QtyPer` float NOT NULL,
  `RequiredQty` float NOT NULL,
  `IssuedQty` float NOT NULL,
  `IUM` varchar(4) NOT NULL,
  `IssuedComplete` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Epicor Vantage - Job Head file.';

-- --------------------------------------------------------

--
-- Table structure for table `ev_meta_log`
--

CREATE TABLE `ev_meta_log` (
  `id` int(11) NOT NULL,
  `timestamp` datetime NOT NULL,
  `type` varchar(20) NOT NULL COMMENT 'Type can be: MRPRUN, DBBACKUP',
  `log` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_part`
--

CREATE TABLE `ev_part` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL COMMENT 'The PartNum field identifies the Part',
  `PartDescription` varchar(2000) NOT NULL COMMENT 'Describes the Part.  This field can not be blank.',
  `Class` varchar(8) NOT NULL COMMENT 'The Inventory class that this Part belongs to. The Class field can be blank or must be valid in the PartClass master file. Classes could be set up for different type of raw materials. It will primarily be used as a report selection parameter.',
  `IUM` varchar(4) NOT NULL COMMENT 'Defines the Unit of Measure used when part is issued, this is also how it is stocked.  Use the value from XaSyst.UM as a default when creating new part records.',
  `PUM` varchar(4) NOT NULL COMMENT 'The Purchasing Unit of measure for the Part.  During Part Maintenance the XaSyst.UM is used as a default for this field. This is used in Purchase Order entry as the default on line item details. ',
  `TypeCode` varchar(2) NOT NULL,
  `MfgComment` varchar(2000) NOT NULL,
  `PurComment` varchar(2000) NOT NULL,
  `MFG` varchar(60) NOT NULL,
  `MFGNo` varchar(60) NOT NULL,
  `Project` varchar(60) NOT NULL,
  `NonStock` tinyint(1) NOT NULL,
  `InActive` tinyint(1) NOT NULL,
  `PhantomBOM` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Epicor Vantage - Part Master file.';

-- --------------------------------------------------------

--
-- Table structure for table `ev_partbin`
--

CREATE TABLE `ev_partbin` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL,
  `BinNum` varchar(10) NOT NULL,
  `OnhandQty` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_partdtl`
--

CREATE TABLE `ev_partdtl` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL COMMENT 'The Part Number of the entry.',
  `RevisionNum` varchar(24) NOT NULL COMMENT 'Part revision number.',
  `PartDescription` varchar(2000) NOT NULL COMMENT 'Describes the Part.',
  `RequirementFlag` tinyint(1) NOT NULL COMMENT 'Indicates if record is a receipt or a requirement.  Used only for sorting receipts before requirements, never shown to user. 1 = Required for the job; 0 = Produced by the job.',
  `DueDate` date DEFAULT NULL COMMENT 'When requirement is needed or receipt is due in.',
  `Quantity` float NOT NULL COMMENT 'The current outstanding of the related supply or demand.',
  `IUM` varchar(4) NOT NULL COMMENT 'Unit of Measure',
  `JobNum` varchar(28) NOT NULL COMMENT 'Job number that record was generated from.',
  `AssemblySeq` int(4) NOT NULL COMMENT 'Job Assembly number that record was generated from.',
  `JobSeq` int(4) NOT NULL COMMENT 'Related Job Material or Operation  Sequence number.',
  `OrderNum` int(4) NOT NULL COMMENT 'Sales Order number of the requirement. Pertains only when Requirement = Yes and SourceFile = OR.',
  `OrderLine` int(4) NOT NULL COMMENT 'Sales Order Line number of the requirement. Pertains only when Requirement = Yes and SourceFile = OR.',
  `PONum` int(4) NOT NULL COMMENT 'Purchase Order number that record was generated from.',
  `POLine` int(4) NOT NULL COMMENT 'Purchase Order Line number that record was generated from.',
  `SourceFile` varchar(4) NOT NULL COMMENT 'Indicates the record type that created this record. JH-JobHead, JA-JobAsmbl, JM-JobMtl, JO-JobOper, PO-PoRels, OR-OrderRels, FC-ForCast.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Epicor Vantage - Part Master file.';

-- --------------------------------------------------------

--
-- Table structure for table `ev_partmtl`
--

CREATE TABLE `ev_partmtl` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL COMMENT 'Parent Part number to which this material item is a component of',
  `RevisionNum` varchar(24) NOT NULL,
  `MtlSeq` int(4) NOT NULL COMMENT 'A sequence number that uniquely defines the Material record within a specific Job/Lot/Assembly. This is system assigned. The next available # is determined by reading last JobMtl record on the Job/Lot/Assembly and then adding one to it.',
  `QtyPer` float NOT NULL,
  `MtlPartNum` varchar(40) NOT NULL,
  `BubbleNum` varchar(20) NOT NULL,
  `FixedQty` tinyint(1) NOT NULL COMMENT 'Indicates if the QtyPer field represents a "Fixed Quantity".  If Yes, then the required quantity = QtyPer.  That is, the quantity does not change as the number of pieces being produced changes.  This can be used to enter Tooling or Fixture type of requirements.',
  `MfgComment` varchar(2000) NOT NULL,
  `PurComment` varchar(2000) NOT NULL,
  `PullAsAsm` tinyint(1) NOT NULL COMMENT 'This is relevant for assemblies (Part.Method = Yes). Indicates that if this assembly should be pulled from stock or manufactured as part of the job it is pulled into. If PullAsAsm = No only the assembly record will be pulled into the job/quote (as a material), the related material and operations will not be pulled over.  ',
  `ViewAsAsm` tinyint(1) NOT NULL COMMENT 'This is relevant for assemblies (Part.Method = Yes). Indicates that if this assembly when shown in ABOM indented views or on reports it should be shown either as a subassembly or material requirement.  If Yes then the assemblies components will be shown else it is shown as a single material requirement line. Similar to the PullAsAsm flag however this is used to control how subassemblies appear in the ABOM module.   NOTE: AS OF 2.70.400 this function is not implemented.  Pending further analysis. It has been added to the schema to make it easier to implement when decision has been reached.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_partopr`
--

CREATE TABLE `ev_partopr` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL,
  `RevisionNum` varchar(24) NOT NULL,
  `OprSeq` int(4) NOT NULL,
  `WCCode` varchar(10) NOT NULL,
  `OpCode` varchar(10) NOT NULL,
  `EstSetHours` float NOT NULL,
  `EstProdHours` float NOT NULL,
  `QtyPer` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_partplant`
--

CREATE TABLE `ev_partplant` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL COMMENT 'The PartNum field identifies the Part and is used in the primary key. ',
  `MinimumQty` float NOT NULL COMMENT 'Indicates the desired minimum on-hand quantity. This is used by the time phase requirements report when user requests to show any parts that will or have fallen below this level. It is also used as a selection parameter for the inventory reorder report. This is an optional field.  ',
  `MaximumQty` float NOT NULL COMMENT 'Use to set a Maximum quantity limit that is desired to be on-hand. This field is used as a selection option by the inventory reorder report to show all parts that are over this limit. This field is optional.',
  `SafetyQty` float NOT NULL COMMENT 'Safety quantity is a purchasing cushion limit. It''s the amount you would need to have to cover your requirements until a shipment arrives from the vendor. If your on-hand quantity falls below this limit it means that there is a good chance that you will run out of material before the next shipment arrives. This value is used by the inventory reorder report and the time phase report. It is an optional field. Note: Safety + Minimum = Reorder Point...  using this formula the  reorder point is the amount at which to reorder to maintain at least the prescribed minimum quantity.',
  `MinOrderQty` float NOT NULL COMMENT 'Used to establish a suggested Order Qty when purchasing this Part for this Plant. This value will be shown on the time phase report.',
  `LeadTime` int(4) NOT NULL COMMENT 'Used to record the normal order lead time for a Part for this Plant. This value is represented in days. It is optional. Used in calculation of suggested order dates, as a default value in job material detail records.',
  `MinMfgLotSize` float NOT NULL COMMENT 'This is the minimum manufacturing lot size.  If the required quantity is less than this amount then MRP will create a job with this production quantity.  The excess amount will be sent to stock.  Zero is no minimum.  If nonzero, this field must be an even multiple of the MfgLotMultiple field.',
  `MinAbc` varchar(2) NOT NULL COMMENT 'When the system sets the A,B,C class, it will not allow the class to be lower then this class.',
  `SystemAbc` varchar(2) NOT NULL COMMENT 'ABC Classification calculated by the system based on Dollar Volume Usage and/or Unit Cost. This is not maintainable by the user.',
  `VendorNum` int(4) NOT NULL COMMENT 'Number of Vendor master that this part is normally purchased from. The Purchase Order Management module uses it.  used in suggested vendor analysis.',
  `MfgLotSize` float NOT NULL COMMENT 'This is the lot size that is used when performing a BOM cost rollup to distribute setup costs.',
  `MaxMfgLotSize` float NOT NULL COMMENT 'This is the maximum manufacturing lot size.  If the required quantity is greater than this amount then MRP will create additional job(s) to satisfy the required production quantity.  Zero is no maximum.  If nonzero, this field must be an even multiple of the MfgLotMultiple field.  Example:  Required Quantity = 500, Maximum Lot Size = 150, 4 jobs will be created with production quantities of 150, 150, 150, and 50.',
  `MfgLotMultiple` float NOT NULL COMMENT 'This is the lot size that is used when performing a BOM cost rollup to distribute setup costs.',
  `ProcessMRP` tinyint(1) NOT NULL COMMENT 'Flag indicating if MRP should process this part.',
  `GenerateSugg` tinyint(1) NOT NULL COMMENT 'Flag indicating if PO suggestion should be generated for this part',
  `DaysOfSupply` int(4) NOT NULL COMMENT 'Used to record the normal order lead time for a Part for this Plant. This value is represented in days. It is optional. Used in calculation of suggested order dates, as a default value in job material detail records.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_partrev`
--

CREATE TABLE `ev_partrev` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL,
  `RevDescription` varchar(2000) NOT NULL,
  `RevisionNum` varchar(24) NOT NULL,
  `Approved` tinyint(1) NOT NULL,
  `ApprovedDate` date DEFAULT NULL,
  `ApprovedBy` varchar(24) NOT NULL,
  `EffectiveDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Part Revision.  Establishes the revisions of a part.';

-- --------------------------------------------------------

--
-- Table structure for table `ev_plantwhse`
--

CREATE TABLE `ev_plantwhse` (
  `id` int(10) UNSIGNED NOT NULL,
  `PartNum` varchar(40) NOT NULL,
  `PrimBin` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_podetail`
--

CREATE TABLE `ev_podetail` (
  `id` int(10) UNSIGNED NOT NULL,
  `VoidLine` tinyint(1) NOT NULL COMMENT 'Indicates if the Line was voided. Voided line items are not maintainable, cannot ""unvoid"".  This field is not directly maintainable.  Instead the void function will be performed via a ""Void Line"" button.  Which then presents a verification dialog box. When an PODetail record is "voided",  all current open  PORel records are also closed and voided.  If no other open PoDetail records exist then set the PoHeader.OperOrder = No. This can also be set when the related PoHeader is voided.',
  `PONUM` int(4) NOT NULL COMMENT 'Purchase order number  that the detail line item is linked to.',
  `OrderQty` float NOT NULL COMMENT 'Total Order Quantity for the line item.  This is stored in the Vendors Unit of Measure. This quantity must always be kept in sync with the scheduled release quantities stored in the PORel table. Normally this field is directly maintainable. However when multiple shipping releases have been established for this line(more than one PORel record) the OrderQty is not maintainable.  As the user modifies the quantities in the individual release lines the OrderQty field will get adjusted. This insures that Order quantity and scheduled  quantities are always in sync. ',
  `IUM` varchar(4) NOT NULL COMMENT 'Issue(Our) Unit Of Measure.',
  `OpenLine` tinyint(1) NOT NULL COMMENT 'Indicates if this line item is Open/Closed. This is not directly maintainable by the user. Normally it gets set to "Closed" as a result of the receiving process. When there are no longer any open PORel records then the PODetail record is closed. This can also be closed when the user Voids the Order or PODetail record.',
  `POLine` int(4) NOT NULL COMMENT 'The line number of the detail record on the purchase order.  This number uniquely identifies the record within the Purchase Order number.  The number not directly maintainable, it is assigned by the system when records are created. The user references this item during PO receipt process.',
  `UnitCost` float NOT NULL COMMENT 'The unit price in the vendors unit of measure.  Unfortunately the Field Name is UnitCost instead of UnitPrice which is a little misleading',
  `CommentText` varchar(2000) DEFAULT NULL COMMENT 'Contains comments about the detail order line item. These will be printed on the purchase order.  Defaults from the related JobOper, JobMtl or Part file.',
  `LineDesc` varchar(2000) NOT NULL COMMENT 'Defaults from JobOper, JobMtl or Part depending on the reference to the job records.  If no job reference then uses the Part.PartDescription if it is a valid PartNum.',
  `PartNum` varchar(40) NOT NULL COMMENT 'OUR internal Part number for this item.',
  `Class` varchar(8) NOT NULL COMMENT 'The foreign key to the PartClass Master. May be blank, if entered must be valid in PartClass file.  Defaulted from Part.Class. The PartClass is used in determining a default G/L expense account. Updated indirectly via a DDSL widget in.',
  `RevisionNum` varchar(24) NOT NULL COMMENT 'OUR revision number of the OUR part.  An optional field. Defaults from the most current  PartRev.RevisionNum.',
  `VendorNum` int(4) NOT NULL COMMENT 'The VendorNum that ties back to the Vendor master file.  This field is a duplicate of the field in POHeader and is maintained  in the write triggers of POHeader and PODetail.',
  `Confirmed` tinyint(1) NOT NULL COMMENT 'Indicated Supplier Confirmed the PO.  Will default from the PO header.',
  `ConfirmDate` date DEFAULT NULL COMMENT 'Date Supplier Confirmed the PO.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_poheader`
--

CREATE TABLE `ev_poheader` (
  `id` int(10) UNSIGNED NOT NULL,
  `EntryPerson` varchar(40) NOT NULL COMMENT 'Person who made the PO entry',
  `OrderDate` date DEFAULT NULL COMMENT 'Order Date for this purchase order. Initially defaults as "today", then defaults as last date entered in this session.',
  `PONum` int(4) NOT NULL COMMENT 'Purchase order number that uniquely identifies the purchase order.',
  `ShipCountry` varchar(100) NOT NULL COMMENT 'Country is used as part of the Ship to  address. It can be left blank.',
  `CommentText` varchar(2000) NOT NULL COMMENT 'Contains comments about over all  purchase order. These will be printed on the purchase order.',
  `VendorNum` int(4) NOT NULL COMMENT 'The VendorNum that ties back to the Vendor master file.  This field is not directly maintainable, instead its assigned via selection list processing.',
  `ShipName` varchar(100) NOT NULL COMMENT 'defaults from the company file.',
  `ShipViaCode` varchar(8) NOT NULL,
  `OpenOrder` tinyint(1) NOT NULL COMMENT 'Indicates if the order is open or closed. This is set automatically when all the PODetail records have been closed or can be set if the user Voids the Order. This field is not directly maintainable.',
  `ShipAddress1` varchar(100) NOT NULL,
  `ShipAddress2` varchar(100) NOT NULL,
  `ExchangeRate` float NOT NULL COMMENT 'Exchange rate that will be used for this order.  Defaults from CurrRate.CurrentRate. Conversion rates will be calculated as System Base = Foreign value * rate, Foreign value = system base * (1/rate). This is the dollar in foreign currency from the exchange rate tables in the newspapers.',
  `ApprovalStatus` varchar(2) NOT NULL COMMENT 'Indicates the approval status of the PO. Valid values are; U - Unsubmitted for Approval,  P - Pending Approval, A - Approved, R - Rejected.  Before a PO can be printed it must be approved.  A PO is consider  approved if it doesn''t exceed the buyers limit or it has been approved by the approver.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_rcvdtl`
--

CREATE TABLE `ev_rcvdtl` (
  `id` int(10) UNSIGNED NOT NULL,
  `PackSlip` varchar(20) NOT NULL COMMENT 'Vendors Packing Slip #.',
  `PartNum` varchar(40) NOT NULL,
  `BinNum` varchar(10) NOT NULL,
  `ReceivedComplete` tinyint(1) NOT NULL COMMENT 'Indicates if this receipt transaction should flag the related purchase order release (PORel) as being received complete (PORel.OpenRelease = No).  When the user toggles this field   Receipt entry considers it  a direct update to the PORel.OpenRelease flag.  What we mean is that the user can change the PORel.OpenRelease flag by maintaining this field on  ANY related receipt transaction for the PORel. Therefore this field should not be used to determine the true status of the PORel record. Receipt Entry allows displays this field based on the current setting of PORel.OpenRelease field. Another point is that if the a receipt transaction is update to a different PO/Line/Release the original PORel will be reopened if there are no other receipt detail records that indicate the release is closed.  All this Open/Close logic occurs in the write trigger of RcvDtl.',
  `PONum` int(4) NOT NULL,
  `VendorQty` float NOT NULL,
  `ReceivedTo` varchar(14) NOT NULL,
  `InspectionReq` tinyint(1) NOT NULL COMMENT 'Indicates if this receipt will be categorized as requiring inspection. It is set to Yes if any of the related Vendor, PartClass, PoDetail, JobMtl, JobOper have their RcvInspectionReq field = Yes.',
  `InspectionPending` tinyint(1) NOT NULL COMMENT 'Indicates if the receipt is pending inspection.',
  `ReceiptDate` date DEFAULT NULL COMMENT 'Receipt date. Mirror image of related RCVHead.ReceiptDate.  Maintained by the RcvHead/RcvDtl write triggers.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ev_vendor`
--

CREATE TABLE `ev_vendor` (
  `id` int(10) UNSIGNED NOT NULL,
  `VendorNum` int(4) NOT NULL,
  `VendorID` varchar(16) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Country` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `inv_kit_instance`
--

CREATE TABLE `inv_kit_instance` (
  `id` int(11) NOT NULL,
  `JobNum` varchar(28) NOT NULL,
  `AssemblySeq` int(11) NOT NULL,
  `IgnoreAsm` varchar(400) NOT NULL,
  `CreatedDate` date NOT NULL,
  `ModifiedDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `inv_kit_print_log`
--

CREATE TABLE `inv_kit_print_log` (
  `id` int(11) NOT NULL,
  `KitInstanceId` int(11) NOT NULL,
  `PrintDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ev_aprvvend`
--
ALTER TABLE `ev_aprvvend`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_jobasmbl`
--
ALTER TABLE `ev_jobasmbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_jobhead`
--
ALTER TABLE `ev_jobhead`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_jobmtl`
--
ALTER TABLE `ev_jobmtl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_meta_log`
--
ALTER TABLE `ev_meta_log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_part`
--
ALTER TABLE `ev_part`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_partbin`
--
ALTER TABLE `ev_partbin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_partdtl`
--
ALTER TABLE `ev_partdtl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_partmtl`
--
ALTER TABLE `ev_partmtl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_partopr`
--
ALTER TABLE `ev_partopr`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_partplant`
--
ALTER TABLE `ev_partplant`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_partrev`
--
ALTER TABLE `ev_partrev`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_plantwhse`
--
ALTER TABLE `ev_plantwhse`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_podetail`
--
ALTER TABLE `ev_podetail`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_poheader`
--
ALTER TABLE `ev_poheader`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_rcvdtl`
--
ALTER TABLE `ev_rcvdtl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ev_vendor`
--
ALTER TABLE `ev_vendor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `inv_kit_instance`
--
ALTER TABLE `inv_kit_instance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `inv_kit_print_log`
--
ALTER TABLE `inv_kit_print_log`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ev_aprvvend`
--
ALTER TABLE `ev_aprvvend`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1979;
--
-- AUTO_INCREMENT for table `ev_jobasmbl`
--
ALTER TABLE `ev_jobasmbl`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3056;
--
-- AUTO_INCREMENT for table `ev_jobhead`
--
ALTER TABLE `ev_jobhead`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35423;
--
-- AUTO_INCREMENT for table `ev_jobmtl`
--
ALTER TABLE `ev_jobmtl`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25125;
--
-- AUTO_INCREMENT for table `ev_meta_log`
--
ALTER TABLE `ev_meta_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ev_part`
--
ALTER TABLE `ev_part`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19927;
--
-- AUTO_INCREMENT for table `ev_partbin`
--
ALTER TABLE `ev_partbin`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6684;
--
-- AUTO_INCREMENT for table `ev_partdtl`
--
ALTER TABLE `ev_partdtl`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20053;
--
-- AUTO_INCREMENT for table `ev_partmtl`
--
ALTER TABLE `ev_partmtl`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=102488;
--
-- AUTO_INCREMENT for table `ev_partopr`
--
ALTER TABLE `ev_partopr`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30012;
--
-- AUTO_INCREMENT for table `ev_partplant`
--
ALTER TABLE `ev_partplant`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45153;
--
-- AUTO_INCREMENT for table `ev_partrev`
--
ALTER TABLE `ev_partrev`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33069;
--
-- AUTO_INCREMENT for table `ev_plantwhse`
--
ALTER TABLE `ev_plantwhse`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45160;
--
-- AUTO_INCREMENT for table `ev_podetail`
--
ALTER TABLE `ev_podetail`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=136422;
--
-- AUTO_INCREMENT for table `ev_poheader`
--
ALTER TABLE `ev_poheader`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34075;
--
-- AUTO_INCREMENT for table `ev_rcvdtl`
--
ALTER TABLE `ev_rcvdtl`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;
--
-- AUTO_INCREMENT for table `ev_vendor`
--
ALTER TABLE `ev_vendor`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1944;
--
-- AUTO_INCREMENT for table `inv_kit_instance`
--
ALTER TABLE `inv_kit_instance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;