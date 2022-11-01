package com.woody.productwarehousing.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderApiUnits {

    @Expose
    @SerializedName("result")
    private List<Result> result;

    @Expose
    @SerializedName("MasterData")
    private List<MasterData> masterData;

    public List<Result> getResult() {
        return result;
    }

    public List<MasterData> getMasterData() {
        return masterData;
    }

    public static class Result {
        @Expose
        @SerializedName("IfSucceed")
        private String ifSucceed;

        @Expose
        @SerializedName("ErrMessage")
        private String errMessage;

        @Expose
        @SerializedName("IfBiz")
        private String ifBiz;

        @Expose
        @SerializedName("ErrMethodName")
        private String errMethodName;

        public String getIfSucceed() {
            return ifSucceed;
        }

        public String getErrMessage() {
            return errMessage;
        }

        public String getIfBiz() {
            return ifBiz;
        }

        public String getErrMethodName() {
            return errMethodName;
        }
    }

    public static class MasterData {

        /**
         * 查詢製令訊息(多品名稱、多品條碼)Response
         * */

        @Expose
        @SerializedName("IsFromMps")
        private int isFromMps;
        @Expose
        @SerializedName("HasCheck")
        private int hasCheck;
        @Expose
        @SerializedName("BadsQty")
        private String badsQty;
        @Expose
        @SerializedName("SplitBillNO")
        private String splitBillNO;
        @Expose
        @SerializedName("FromRowNO")
        private int fromRowNO;
        @Expose
        @SerializedName("EUnitRelation")
        private String eUnitRelation;
        @Expose
        @SerializedName("SrcProducerName")
        private String srcProducerName;
        @Expose
        @SerializedName("StrictLimit")
        private int strictLimit;
        @Expose
        @SerializedName("DepartID")
        private String departID;
        @Expose
        @SerializedName("Price")
        private String price;
        @Expose
        @SerializedName("SrcNoInQty")
        private String srcNoInQty;
        @Expose
        @SerializedName("UnitGood")
        private String unitGood;
        @Expose
        @SerializedName("WareInType")
        private int wareInType;
        @Expose
        @SerializedName("SrcProcRowNo")
        private int srcProcRowNo;
        @Expose
        @SerializedName("SUnitID")
        private String sUnitID;
        @Expose
        @SerializedName("SrcMkPgmName")
        private String srcMkPgmName;
        @Expose
        @SerializedName("GoodsQty")
        private String goodsQty;
        @Expose
        @SerializedName("FunctionaryID")
        private String functionaryID;
        @Expose
        @SerializedName("CompleteStatus")
        private int completeStatus;
        @Expose
        @SerializedName("EstGoodsRdyDate")
        private int estGoodsRdyDate;
        @Expose
        @SerializedName("Weight")
        private String weight;
        @Expose
        @SerializedName("EstMatRdyDate")
        private int estMatRdyDate;
        @Expose
        @SerializedName("BillStatus")
        private int billStatus;
        @Expose
        @SerializedName("SrcMkOrdType")
        private int srcMkOrdType;
        @Expose
        @SerializedName("LevelId")
        private String levelId;
        @Expose
        @SerializedName("SrcMkOrdDate")
        private int srcMkOrdDate;
        @Expose
        @SerializedName("BoxQty")
        private int boxQty;
        @Expose
        @SerializedName("SQtyGood")
        private String sQtyGood;
        @Expose
        @SerializedName("HasBillAttach")
        private int hasBillAttach;
        @Expose
        @SerializedName("HadWareIn")
        private int hadWareIn;
        @Expose
        @SerializedName("SplitFromType")
        private int splitFromType;
        @Expose
        @SerializedName("EndCaseDate")
        private int endCaseDate;
        @Expose
        @SerializedName("MakerID")
        private String makerID;
        @Expose
        @SerializedName("Quantity")
        private String quantity;
        @Expose
        @SerializedName("UnitID")
        private String unitID;
        @Expose
        @SerializedName("SplitFromNO")
        private String splitFromNO;
        @Expose
        @SerializedName("SrcProductName")
        private String srcProductName;
        @Expose
        @SerializedName("MatsCanBeChanged")
        private int matsCanBeChanged;
        @Expose
        @SerializedName("OutPackPrint")
        private String outPackPrint;
        @Expose
        @SerializedName("EstGoodsArrDate")
        private int estGoodsArrDate;
        @Expose
        @SerializedName("ProducerName")
        private String producerName;
        @Expose
        @SerializedName("DetailData")
        private List<DetailData> detailData;
        @Expose
        @SerializedName("EstMatArrDate")
        private int estMatArrDate;
        @Expose
        @SerializedName("Money")
        private String money;
        @Expose
        @SerializedName("Maker")
        private String maker;
        @Expose
        @SerializedName("SUnitBad")
        private String sUnitBad;
        @Expose
        @SerializedName("PermitterID")
        private String permitterID;
        @Expose
        @SerializedName("ProdtStart")
        private int prodtStart;
        @Expose
        @SerializedName("SamleName")
        private String samleName;
        @Expose
        @SerializedName("ProdtSchNo")
        private String prodtSchNo;
        @Expose
        @SerializedName("BoxName")
        private String boxName;
        @Expose
        @SerializedName("BomSourceType")
        private int bomSourceType;
        @Expose
        @SerializedName("BatchAmount")
        private String batchAmount;
        @Expose
        @SerializedName("MulWareInType")
        private int mulWareInType;
        @Expose
        @SerializedName("MergeOutState")
        private int mergeOutState;
        @Expose
        @SerializedName("SrcMkPgmSerNo")
        private int srcMkPgmSerNo;
        @Expose
        @SerializedName("Permitter")
        private String permitter;
        @Expose
        @SerializedName("MakeType")
        private int makeType;
        @Expose
        @SerializedName("EUnitID")
        private String eUnitID;
        @Expose
        @SerializedName("HadTakeMat")
        private int hadTakeMat;
        @Expose
        @SerializedName("ProjectName")
        private String projectName;
        @Expose
        @SerializedName("MainMark")
        private String mainMark;
        @Expose
        @SerializedName("Producer")
        private String producer;
        @Expose
        @SerializedName("ProdtQty")
        private String prodtQty;
        @Expose
        @SerializedName("ProdtEnd")
        private int prodtEnd;
        @Expose
        @SerializedName("CheckProjectID")
        private String checkProjectID;
        @Expose
        @SerializedName("CurrID")
        private String currID;
        @Expose
        @SerializedName("ProdtAtHol")
        private int prodtAtHol;
        @Expose
        @SerializedName("ProductType")
        private int productType;
        @Expose
        @SerializedName("QtyPBox")
        private String qtyPBox;
        @Expose
        @SerializedName("ProductName")
        private String productName;
        @Expose
        @SerializedName("ReMkWareInQty")
        private String reMkWareInQty;
        @Expose
        @SerializedName("ProdtStatus")
        private int prodtStatus;
        @Expose
        @SerializedName("SrcAllQty")
        private String srcAllQty;
        @Expose
        @SerializedName("SourceType")
        private int sourceType;
        @Expose
        @SerializedName("ReMkOrdType")
        private int reMkOrdType;
        @Expose
        @SerializedName("IsOdd")
        private int isOdd;
        @Expose
        @SerializedName("IsNCT")
        private int isNCT;
        @Expose
        @SerializedName("SrcProductID")
        private String srcProductID;
        @Expose
        @SerializedName("EQuantity")
        private String eQuantity;
        @Expose
        @SerializedName("EstTakeMatDate")
        private int estTakeMatDate;
        @Expose
        @SerializedName("EstStockOutDate")
        private int estStockOutDate;
        @Expose
        @SerializedName("LevelName")
        private String levelName;
        @Expose
        @SerializedName("ExchRate")
        private String exchRate;
        @Expose
        @SerializedName("CalcBtn")
        private int calcBtn;
        @Expose
        @SerializedName("UnitBad")
        private String unitBad;
        @Expose
        @SerializedName("MatchNO")
        private String matchNO;
        @Expose
        @SerializedName("HasExecMRP")
        private int hasExecMRP;
        @Expose
        @SerializedName("IsTaxProdt")
        private int isTaxProdt;
        @Expose
        @SerializedName("ChangeDate")
        private int changeDate;
        @Expose
        @SerializedName("SrcUnit")
        private String srcUnit;
        @Expose
        @SerializedName("UnitRelation")
        private String unitRelation;
        @Expose
        @SerializedName("ProductID")
        private String productID;
        @Expose
        @SerializedName("CostType")
        private int costType;
        @Expose
        @SerializedName("CheckMethod")
        private int checkMethod;
        @Expose
        @SerializedName("IsPrdGradeBom")
        private int isPrdGradeBom;
        @Expose
        @SerializedName("EstWareInDate")
        private int estWareInDate;
        @Expose
        @SerializedName("IsConfigMat")
        private int isConfigMat;
        @Expose
        @SerializedName("UseProducerCapa")
        private int useProducerCapa;
        @Expose
        @SerializedName("DepartName")
        private String departName;
        @Expose
        @SerializedName("SingleWeight")
        private String singleWeight;
        @Expose
        @SerializedName("Remark")
        private String remark;
        @Expose
        @SerializedName("FunctionaryName")
        private String functionaryName;
        @Expose
        @SerializedName("ProjectID")
        private String projectID;
        @Expose
        @SerializedName("UDef2")
        private String uDef2;
        @Expose
        @SerializedName("UDef1")
        private String uDef1;
        @Expose
        @SerializedName("DetailData1")
        private List<DetailData1> detailData1;
        @Expose
        @SerializedName("SampleID")
        private String sampleID;
        @Expose
        @SerializedName("SideMark")
        private String sideMark;
        @Expose
        @SerializedName("CheckProjectName")
        private String checkProjectName;
        @Expose
        @SerializedName("EUnit")
        private String eUnit;
        @Expose
        @SerializedName("SUnitGood")
        private String sUnitGood;
        @Expose
        @SerializedName("MKOrdDate")
        private int mKOrdDate;
        @Expose
        @SerializedName("ChargeMode")
        private int chargeMode;
        @Expose
        @SerializedName("OriginProdtQty")
        private String originProdtQty;
        @Expose
        @SerializedName("SrcMkPgmID")
        private String srcMkPgmID;
        @Expose
        @SerializedName("SourceNo")
        private String sourceNo;
        @Expose
        @SerializedName("EstPrepMatDate")
        private int estPrepMatDate;
        @Expose
        @SerializedName("SumQuantity")
        private String sumQuantity;
        @Expose
        @SerializedName("ProductUnit_L")
        private String productUnit_L;
        @Expose
        @SerializedName("SrcProducer")
        private String srcProducer;
        @Expose
        @SerializedName("SrcProdtQty")
        private String srcProdtQty;
        @Expose
        @SerializedName("ProcCanBeChanged")
        private int procCanBeChanged;
        @Expose
        @SerializedName("EngUnit")
        private String engUnit;
        @Expose
        @SerializedName("SQuantity")
        private String sQuantity;
        @Expose
        @SerializedName("SUnit")
        private String sUnit;
        @Expose
        @SerializedName("SrcMkOrdNo")
        private String srcMkOrdNo;
        @Expose
        @SerializedName("SrcProducerClass")
        private int srcProducerClass;
        @Expose
        @SerializedName("ReMkOrdNo")
        private String reMkOrdNo;
        @Expose
        @SerializedName("SingleUnit")
        private String singleUnit;
        @Expose
        @SerializedName("SplitStatus")
        private int splitStatus;
        @Expose
        @SerializedName("InnerPackPrint")
        private String innerPackPrint;
        @Expose
        @SerializedName("MKOrdNO")
        private String mKOrdNO;
        @Expose
        @SerializedName("CustBill")
        private String custBill;
        @Expose
        @SerializedName("PrdGradeCanBeChanged")
        private int prdGradeCanBeChanged;
        @Expose
        @SerializedName("BoxID")
        private String boxID;
        @Expose
        @SerializedName("SQtyBad")
        private String sQtyBad;
        @Expose
        @SerializedName("NoPowerToViewAdvPerms")
        private int noPowerToViewAdvPerms;

        public List<DetailData> getDetailData() {
            return detailData;
        }

        public List<DetailData1> getDetailData1() {
            return detailData1;
        }

        public String getMKOrdNO() {
            return mKOrdNO;
        }

        public int getMKOrdDate() {
            return mKOrdDate;
        }

        public String getProductID() {
            return productID;
        }

        public String getProductName() {
            return productName;
        }

        public String getSrcNoInQty() {
            return srcNoInQty;
        }

        public int getEstWareInDate() {
            return estWareInDate;
        }

        public int getBillStatus() {
            return billStatus;
        }

        /**
         * 查多品詳情(定重、保存天數)Response，有些參數在上面已存在，所以不寫
         * */

        @Expose
        @SerializedName("FCMaterialID")
        private String fCMaterialID;
        @Expose
        @SerializedName("AUnit")
        private String aUnit;
        @Expose
        @SerializedName("LatestOutDate")
        private int latestOutDate;
        @Expose
        @SerializedName("MEAMTUnit")
        private String mEAMTUnit;
        @Expose
        @SerializedName("FCPackStyle")
        private int fCPackStyle;
        @Expose
        @SerializedName("CtmLostEnd")
        private String ctmLostEnd;
        @Expose
        @SerializedName("IsOptional")
        private int isOptional;
        @Expose
        @SerializedName("BaseInc")
        private String baseInc;
        @Expose
        @SerializedName("MaterialWare")
        private String materialWare;
        @Expose
        @SerializedName("LatestPurchDate")
        private int latestPurchDate;
        @Expose
        @SerializedName("CBorrowAmt")
        private String cBorrowAmt;
        @Expose
        @SerializedName("LLC")
        private int lLC;
        @Expose
        @SerializedName("BStdCost")
        private String bStdCost;
        @Expose
        @SerializedName("WareType")
        private int wareType;
        @Expose
        @SerializedName("UseSmallBatch")
        private int useSmallBatch;
        @Expose
        @SerializedName("BSafeAllStk")
        private String bSafeAllStk;
        @Expose
        @SerializedName("CAvgCost")
        private String cAvgCost;
        @Expose
        @SerializedName("DataVer")
        private int dataVer;
        @Expose
        @SerializedName("IsCharacter")
        private int isCharacter;
        @Expose
        @SerializedName("FCTransCondition")
        private int fCTransCondition;
        @Expose
        @SerializedName("UnitConver")
        private String unitConver;
        @Expose
        @SerializedName("EffectDateUsed")
        private int effectDateUsed;
        @Expose
        @SerializedName("ExcelMergeIn")
        private int excelMergeIn;
        @Expose
        @SerializedName("TakeMatMultiple")
        private String takeMatMultiple;
        @Expose
        @SerializedName("InvoProdName")
        private String invoProdName;
        @Expose
        @SerializedName("BAllCost")
        private String bAllCost;
        @Expose
        @SerializedName("IsConfig")
        private int isConfig;
        @Expose
        @SerializedName("MinPurch")
        private String minPurch;
        private String standard;
        @Expose
        @SerializedName("BatchUsed")
        private int batchUsed;
        @Expose
        @SerializedName("FCBusinessName")
        private int fCBusinessName;
        @Expose
        @SerializedName("FCProperty")
        private int fCProperty;
        @Expose
        @SerializedName("DefValidDays")
        private int defValidDays;
        @Expose
        @SerializedName("BCurrStock")
        private String bCurrStock;
        @Expose
        @SerializedName("FCBigClassID")
        private String fCBigClassID;
        @Expose
        @SerializedName("UseAuditing")
        private int useAuditing;
        @Expose
        @SerializedName("FOBPrice")
        private String fOBPrice;
        @Expose
        @SerializedName("UserUnitConver")
        private String userUnitConver;
        @Expose
        @SerializedName("MergerNo")
        private String mergerNo;
        @Expose
        @SerializedName("NUnit")
        private String nUnit;
        @Expose
        @SerializedName("SuggestPrice")
        private String suggestPrice;
        @Expose
        @SerializedName("NetWeightUnit")
        private String netWeightUnit;
        @Expose
        @SerializedName("BOutStockDay")
        private int bOutStockDay;
        @Expose
        @SerializedName("CCurrStock")
        private String cCurrStock;
        @Expose
        @SerializedName("UseChar")
        private int useChar;
        @Expose
        @SerializedName("InsurRate")
        private String insurRate;
        @Expose
        @SerializedName("FCOutPackStyle")
        private String fCOutPackStyle;
        @Expose
        @SerializedName("MEAMT")
        private String mEAMT;
        @Expose
        @SerializedName("FCMaterialRemark")
        private String fCMaterialRemark;
        @Expose
        @SerializedName("BusiTaxRate")
        private String busiTaxRate;
        @Expose
        @SerializedName("SafeStock")
        private int safeStock;
        @Expose
        @SerializedName("LatestSalesDate")
        private int latestSalesDate;
        @Expose
        @SerializedName("UseSerial")
        private int useSerial;
        @Expose
        @SerializedName("FCThinClassID")
        private String fCThinClassID;
        @Expose
        @SerializedName("BAvgCost")
        private String bAvgCost;
        @Expose
        @SerializedName("PackUnit2")
        private String packUnit2;
        @Expose
        @SerializedName("PackUnit1")
        private String packUnit1;
        @Expose
        @SerializedName("FCLogID")
        private String fCLogID;
        @Expose
        @SerializedName("ImpTaxRate")
        private String impTaxRate;
        @Expose
        @SerializedName("NotIncMRP")
        private int notIncMRP;
        @Expose
        @SerializedName("FCMaterialName")
        private String fCMaterialName;
        @Expose
        @SerializedName("LUnit")
        private String lUnit;
        @Expose
        @SerializedName("ConverUnit")
        private String converUnit;
        @Expose
        @SerializedName("ConverRate")
        private String converRate;
        @Expose
        @SerializedName("ProdCodeRuleType")
        private String prodCodeRuleType;
        @Expose
        @SerializedName("LawUnitID")
        private String lawUnitID;
        @Expose
        @SerializedName("PurchPolicy")
        private int purchPolicy;
        @Expose
        @SerializedName("FCMakeFactID")
        private String fCMakeFactID;
        @Expose
        @SerializedName("CodeRuleName")
        private String codeRuleName;
        @Expose
        @SerializedName("CodeRule")
        private String codeRule;
        @Expose
        @SerializedName("InPackUnit")
        private String inPackUnit;
        @Expose
        @SerializedName("FCFactoryName")
        private String fCFactoryName;
        @Expose
        @SerializedName("BPurchDate")
        private int bPurchDate;
        @Expose
        @SerializedName("FCThinClassName")
        private String fCThinClassName;
        @Expose
        @SerializedName("GrossWeigh")
        private String grossWeigh;
        @Expose
        @SerializedName("MVolume")
        private String mVolume;
        @Expose
        @SerializedName("FCAreaName")
        private String fCAreaName;
        @Expose
        @SerializedName("Main_Des")
        private String main_Des;
        @Expose
        @SerializedName("FCIsInsition")
        private int fCIsInsition;
        @Expose
        @SerializedName("GWeight")
        private String gWeight;
        @Expose
        @SerializedName("CustodyUnitID")
        private String custodyUnitID;
        @Expose
        @SerializedName("MainRela")
        private int mainRela;
        @Expose
        @SerializedName("FCFactoryID")
        private String fCFactoryID;
        @Expose
        @SerializedName("FCBulkRemark")
        private String fCBulkRemark;
        @Expose
        @SerializedName("ProdName")
        private String prodName;
        @Expose
        @SerializedName("AdvanceDays")
        private String advanceDays;
        @Expose
        @SerializedName("ProdForm")
        private int prodForm;
        @Expose
        @SerializedName("ProdDesc")
        private String prodDesc;
        @Expose
        @SerializedName("TraBoxQty")
        private String traBoxQty;
        @Expose
        @SerializedName("DefCheckerID")
        private String defCheckerID;
        @Expose
        @SerializedName("UpdateKey")
        private int updateKey;
        @Expose
        @SerializedName("OverReceRate")
        private String overReceRate;
        @Expose
        @SerializedName("IsNotMaterArea")
        private int isNotMaterArea;
        @Expose
        @SerializedName("UserUnitConver2")
        private String userUnitConver2;
        @Expose
        @SerializedName("UserUnitConver1")
        private String userUnitConver1;
        @Expose
        @SerializedName("BTotalCost")
        private String bTotalCost;
        @Expose
        @SerializedName("CtmUnit")
        private String ctmUnit;
        @Expose
        @SerializedName("PriceOfTax")
        private int priceOfTax;
        @Expose
        @SerializedName("ValidDateUsed")
        private int validDateUsed;
        @Expose
        @SerializedName("OutPackUnit")
        private String outPackUnit;
        @Expose
        @SerializedName("FCSmallClassName")
        private String fCSmallClassName;
        @Expose
        @SerializedName("CtmWeight")
        private String ctmWeight;
        @Expose
        @SerializedName("ProdID")
        private String prodID;
        @Expose
        @SerializedName("NetWeight")
        private String netWeight;
        @Expose
        @SerializedName("IsVirtual")
        private int isVirtual;
        @Expose
        @SerializedName("FCPackUnitName")
        private String fCPackUnitName;
        @Expose
        @SerializedName("SalesPriceE")
        private String salesPriceE;
        @Expose
        @SerializedName("SalesPriceD")
        private String salesPriceD;
        @Expose
        @SerializedName("SalesPriceC")
        private String salesPriceC;
        @Expose
        @SerializedName("SalesPriceB")
        private String salesPriceB;
        @Expose
        @SerializedName("SalesPriceA")
        private String salesPriceA;
        @Expose
        @SerializedName("CanModifyCheck")
        private int canModifyCheck;
        @Expose
        @SerializedName("CTotalCost")
        private String cTotalCost;
        @Expose
        @SerializedName("FCGeneRemark")
        private int fCGeneRemark;
        @Expose
        @SerializedName("ProdGraph")
        private String prodGraph;
        @Expose
        @SerializedName("VolumeUnit")
        private String volumeUnit;
        @Expose
        @SerializedName("BSalesDate")
        private int bSalesDate;
        @Expose
        @SerializedName("TypeName")
        private String typeName;
        @Expose
        @SerializedName("CommodityID")
        private String commodityID;
        @Expose
        @SerializedName("CLendAmt")
        private String cLendAmt;
        @Expose
        @SerializedName("PerDays")
        private int perDays;
        @Expose
        @SerializedName("Width")
        private String width;
        @Expose
        @SerializedName("Excise")
        private String excise;
        @Expose
        @SerializedName("NeedDeclare")
        private int needDeclare;
        @Expose
        @SerializedName("LatestIndate")
        private int latestIndate;
        @Expose
        @SerializedName("BackTaxRate")
        private String backTaxRate;
        @Expose
        @SerializedName("IsOptionalManual")
        private int isOptionalManual;
        @Expose
        @SerializedName("UsePurseCheck")
        private int usePurseCheck;
        @Expose
        @SerializedName("GUnit")
        private String gUnit;
        @Expose
        @SerializedName("MajorSupplier")
        private String majorSupplier;
        @Expose
        @SerializedName("ProdPic")
        private String prodPic;
        @Expose
        @SerializedName("ProdReviewID")
        private String prodReviewID;
        @Expose
        @SerializedName("BAllAmt")
        private String bAllAmt;
        @Expose
        @SerializedName("GrossWeightUnit")
        private String grossWeightUnit;
        @Expose
        @SerializedName("StdPrice")
        private String stdPrice;
        @Expose
        @SerializedName("QJSetWeight")
        private String qJSetWeight;
        @Expose
        @SerializedName("BInStockDay")
        private int bInStockDay;
        @Expose
        @SerializedName("FCChargeFtyID")
        private String fCChargeFtyID;
        @Expose
        @SerializedName("FCAreaID")
        private String fCAreaID;
        @Expose
        @SerializedName("VUnit")
        private String vUnit;
        @Expose
        @SerializedName("IsLeftOverMat")
        private int isLeftOverMat;
        @Expose
        @SerializedName("FCMidClassID")
        private String fCMidClassID;
        @Expose
        @SerializedName("QJUppLimitWt")
        private String qJUppLimitWt;
        @Expose
        @SerializedName("InvoName")
        private String invoName;
        @Expose
        @SerializedName("FCUnit")
        private int fCUnit;
        @Expose
        @SerializedName("FCSale")
        private int fCSale;
        @Expose
        @SerializedName("ClassName")
        private String className;
        @Expose
        @SerializedName("CAllAmt")
        private String cAllAmt;
        @Expose
        @SerializedName("InPackAmt")
        private String inPackAmt;
        @Expose
        @SerializedName("F5Btn")
        private int f5Btn;
        @Expose
        @SerializedName("BarCodeID")
        private String barCodeID;
        @Expose
        @SerializedName("InsurRateEx")
        private String insurRateEx;
        @Expose
        @SerializedName("DetailData2")
        private List<Object> detailData2;
        @Expose
        @SerializedName("NWeight")
        private String nWeight;
        @Expose
        @SerializedName("CommodityType")
        private int commodityType;
        @Expose
        @SerializedName("CommodityName")
        private String commodityName;
        @Expose
        @SerializedName("FCChargeFtyName")
        private String fCChargeFtyName;
        @Expose
        @SerializedName("ClassID")
        private String classID;
        @Expose
        @SerializedName("IsOptionalAuto")
        private int isOptionalAuto;
        @Expose
        @SerializedName("Volume")
        private String volume;
        @Expose
        @SerializedName("CtmCurrID")
        private String ctmCurrID;
        @Expose
        @SerializedName("BLendAmt")
        private String bLendAmt;
        @Expose
        @SerializedName("IsCheck")
        private int isCheck;
        @Expose
        @SerializedName("MemberPrice")
        private String memberPrice;
        @Expose
        @SerializedName("DefCheckerName")
        private String defCheckerName;
        @Expose
        @SerializedName("DefValidDay")
        private int defValidDay;
        @Expose
        @SerializedName("FCMidClassName")
        private String fCMidClassName;
        @Expose
        @SerializedName("Unit")
        private String unit;
        @Expose
        @SerializedName("WareHouseName")
        private String wareHouseName;
        @Expose
        @SerializedName("Commoditystandard")
        private String commoditystandard;
        @Expose
        @SerializedName("Long")
        private String apiLong;
        @Expose
        @SerializedName("LawUnitIDB")
        private String lawUnitIDB;
        @Expose
        @SerializedName("High")
        private String high;
        @Expose
        @SerializedName("HQ40")
        private String hQ40;
        @Expose
        @SerializedName("CY45")
        private String cY45;
        @Expose
        @SerializedName("CY40")
        private String cY40;
        @Expose
        @SerializedName("CY20")
        private String cY20;
        @Expose
        @SerializedName("Area")
        private String area;
        @Expose
        @SerializedName("InvalidDate")
        private int invalidDate;
        @Expose
        @SerializedName("FCBigClassName")
        private String fCBigClassName;
        @Expose
        @SerializedName("UseInCtoms")
        private int useInCtoms;
        @Expose
        @SerializedName("SubID")
        private String subID;
        @Expose
        @SerializedName("MoreRate")
        private String moreRate;
        @Expose
        @SerializedName("CStdCost")
        private String cStdCost;
        @Expose
        @SerializedName("EngName")
        private String engName;
        @Expose
        @SerializedName("UseOverReceRate")
        private int useOverReceRate;
        @Expose
        @SerializedName("SluggishDays")
        private int sluggishDays;
        @Expose
        @SerializedName("PackAmt2")
        private String packAmt2;
        @Expose
        @SerializedName("PackAmt1")
        private String packAmt1;
        @Expose
        @SerializedName("FCPackWeight")
        private String fCPackWeight;
        @Expose
        @SerializedName("FCTransReMark")
        private int fCTransReMark;
        @Expose
        @SerializedName("FCSmallClassID")
        private String fCSmallClassID;
        @Expose
        @SerializedName("FOBCurrID")
        private String fOBCurrID;
        @Expose
        @SerializedName("CCC_CODE")
        private String cCC_CODE;
        @Expose
        @SerializedName("UsePerms")
        private int usePerms;
        @Expose
        @SerializedName("TraBoxUnit")
        private String traBoxUnit;
        @Expose
        @SerializedName("CtmLeftover")
        private int ctmLeftover;
        @Expose
        @SerializedName("StdCost")
        private String stdCost;
        @Expose
        @SerializedName("CSafeAllStk")
        private String cSafeAllStk;
        @Expose
        @SerializedName("CtmLostBegin")
        private String ctmLostBegin;
        @Expose
        @SerializedName("InventoryID")
        private String inventoryID;
        @Expose
        @SerializedName("FCFactProdID")
        private String fCFactProdID;
        @Expose
        @SerializedName("OutPackAmt")
        private String outPackAmt;
        @Expose
        @SerializedName("UseProdtCheck")
        private int useProdtCheck;
        @Expose
        @SerializedName("QJLowLimitWt")
        private String qJLowLimitWt;
        @Expose
        @SerializedName("MajorSupplierName")
        private String majorSupplierName;
        @Expose
        @SerializedName("CAllCost")
        private String cAllCost;
        @Expose
        @SerializedName("GWMatID")
        private String gWMatID;
        @Expose
        @SerializedName("FCPackUnitID")
        private String fCPackUnitID;
        @Expose
        @SerializedName("CodeRuleIsOptional")
        private int codeRuleIsOptional;
        @Expose
        @SerializedName("TempDelete")
        private int tempDelete;
        @Expose
        @SerializedName("BBorrowAmt")
        private String bBorrowAmt;
        @Expose
        @SerializedName("RowNo")
        private int rowNo;

        public String getProdName() {
            return prodName;
        }

        public String getProdID() {
            return prodID;
        }

        public String getQJSetWeight() {
            return qJSetWeight;
        }

        public String getQJUppLimitWt() {
            return qJUppLimitWt;
        }

        public String getQJLowLimitWt() {
            return qJLowLimitWt;
        }

        public int getDefValidDays() {
            return defValidDays;
        }

        public int getValidDateUsed() {
            return validDateUsed;
        }

        /**
         * 上傳結果Response，有些參數在上面已存在，所以不寫
         * */
        @Expose
        @SerializedName("WareInNO")
        private String WareInNO;

        public String getWareInNO() {
            return WareInNO;
        }
    }

    public static class DetailData {
        @Expose
        @SerializedName("ReplaceProdName")
        private String replaceProdName;
        @Expose
        @SerializedName("ReplaceProdID")
        private String replaceProdID;
        @Expose
        @SerializedName("OughtQty")
        private String oughtQty;
        @Expose
        @SerializedName("CharComb")
        private String charComb;
        @Expose
        @SerializedName("HasPic")
        private int hasPic;
        @Expose
        @SerializedName("Detail")
        private String detail;
        @Expose
        @SerializedName("TakeMatMultiple")
        private String takeMatMultiple;
        @Expose
        @SerializedName("IsConfig")
        private int isConfig;
        @Expose
        @SerializedName("ProcRowNO")
        private int procRowNO;
        @Expose
        @SerializedName("UnitOughtQty")
        private String unitOughtQty;
        @Expose
        @SerializedName("CharactersNameComb")
        private String charactersNameComb;
        @Expose
        @SerializedName("SubConfigNo")
        private String subConfigNo;
        @Expose
        @SerializedName("MergeSubQty")
        private String mergeSubQty;
        @Expose
        @SerializedName("MatsRemark")
        private String matsRemark;
        @Expose
        @SerializedName("UseSerial")
        private int useSerial;
        @Expose
        @SerializedName("BatchAmount")
        private String batchAmount;
        @Expose
        @SerializedName("MatSource")
        private int matSource;
        @Expose
        @SerializedName("VirtualParentID")
        private String virtualParentID;
        @Expose
        @SerializedName("ProdName")
        private String prodName;
        @Expose
        @SerializedName("OriginalQty")
        private String originalQty;
        @Expose
        @SerializedName("IsTaxProdt")
        private int isTaxProdt;
        @Expose
        @SerializedName("MatForm")
        private int matForm;
        @Expose
        @SerializedName("SubProdID")
        private String subProdID;
        @Expose
        @SerializedName("IsSubstituted")
        private int isSubstituted;
        @Expose
        @SerializedName("MainConfigNo")
        private String mainConfigNo;
        @Expose
        @SerializedName("MainCharComb")
        private String mainCharComb;
        @Expose
        @SerializedName("MainCharactersNameComb")
        private String mainCharactersNameComb;
        @Expose
        @SerializedName("MkOrdNO")
        private String mkOrdNO;
        @Expose
        @SerializedName("Unit")
        private String unit;
        @Expose
        @SerializedName("UseInCtoms")
        private int useInCtoms;
        @Expose
        @SerializedName("SerNO")
        private int serNO;
        @Expose
        @SerializedName("WestingRate")
        private String westingRate;
        @Expose
        @SerializedName("RowNO")
        private int rowNO;
        @Expose
        @SerializedName("QtyOfBatch")
        private String qtyOfBatch;
        @Expose
        @SerializedName("NoPowerToViewAdvPerms")
        private int noPowerToViewAdvPerms;

    }

    public static class DetailData1 {
        @Expose
        @SerializedName("EUnitRelation")
        private String eUnitRelation;
        @Expose
        @SerializedName("SUnitID")
        private String sUnitID;
        @Expose
        @SerializedName("NCTProdID")
        private String nCTProdID;
        @Expose
        @SerializedName("UnitId")
        private String unitId;
        @Expose
        @SerializedName("CostPercent")
        private String costPercent;
        @Expose
        @SerializedName("EUnitID")
        private String eUnitID;
        @Expose
        @SerializedName("ProdName")
        private String prodName;
        @Expose
        @SerializedName("QtyOfSet")
        private String qtyOfSet;
        @Expose
        @SerializedName("EQuantity")
        private String eQuantity;
        @Expose
        @SerializedName("BillNo")
        private String billNo;
        @Expose
        @SerializedName("CalcBtn")
        private int calcBtn;
        @Expose
        @SerializedName("UnitRelation")
        private String unitRelation;
        @Expose
        @SerializedName("IsPrdGradeBom")
        private int isPrdGradeBom;
        @Expose
        @SerializedName("SingleWeight")
        private String singleWeight;
        @Expose
        @SerializedName("Remark")
        private String remark;
        @Expose
        @SerializedName("sCalcBtn")
        private int sCalcBtn;
        @Expose
        @SerializedName("EUnit")
        private String eUnit;
        @Expose
        @SerializedName("Unit")
        private String unit;
        @Expose
        @SerializedName("SerNo")
        private int serNo;
        @Expose
        @SerializedName("SQuantity")
        private String sQuantity;
        @Expose
        @SerializedName("SUnit")
        private String sUnit;
        @Expose
        @SerializedName("SingleUnit")
        private String singleUnit;
        @Expose
        @SerializedName("RowNo")
        private int rowNo;
        @Expose
        @SerializedName("NoPowerToViewAdvPerms")
        private int noPowerToViewAdvPerms;

        public String getProdName() {
            return prodName;
        }

        public String getNCTProdID() {
            return nCTProdID;
        }

        public int getRowNo() {
            return rowNo;
        }
    }
}
