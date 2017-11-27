using System.Collections.Generic;

namespace Cashew.Internal.Loment.Dto
{
    internal class ResponseUtil
    {
        internal const int UnknownCodeKey = -1;
        private static readonly Dictionary<int, ResponseMeta> MetaMap = new Dictionary<int, ResponseMeta>();

        private const string LomentCode_0 = "Success";
        private const string LomentCode_1 = "Failure";
        private const string LomentCode_10002 = "User account not found";
        private const string LomentCode_10003 = "User account inactive";
        private const string LomentCode_10102 = "Duplicate username";
        private const string LomentCode_10103 = "Duplicate mobile number";
        private const string LomentCode_10104 = "Invalid data(invalid email address format / mobile number and given country do not match)";
        private const string LomentCode_10105 = "Registration successful.But, there are other accounts using the same mobile number";
        private const string LomentCode_10202 = "Invalid email";
        private const string LomentCode_10203 = "Invalid password";
        private const string LomentCode_10204 = "Account inactive";
        private const string LomentCode_10304 = "Invalid data(invalid email address format/ mobile number and given country do not match)";
        private const string LomentCode_10402 = "Invalid key";
        private const string LomentCode_10403 = "Expired key";
        private const string LomentCode_20002 = "Device not found";
        private const string LomentCode_20003 = "Device inactive";
        private const string LomentCode_20102 = "Duplicate device id";
        private const string LomentCode_20103 = "Duplicate device name";
        private const string LomentCode_20104 = "Device type not known";
        private const string LomentCode_20105 = "Device id invalid";
        private const string LomentCode_20202 = "Duplicate device id";
        private const string LomentCode_20203 = "Duplicate device name";
        private const string LomentCode_20204 = "Device type not known";
        private const string LomentCode_20205 = "Device id invalid";
        private const string LomentCode_20206 = "Invalid device status given";
        private const string LomentCode_30002 = "Account not found";
        private const string LomentCode_30003 = "Account inactive";
        private const string LomentCode_30102 = "Account name duplicate / Invalid phone number";
        private const string LomentCode_30103 = "Account provider not known / Username duplicate";
        private const string LomentCode_30104 = "Account server protocol not known";
        private const string LomentCode_30105 = "Account server SSL Enabled / Disabled setting invalid";
        private const string LomentCode_30106 = "Account server port setting invalid";
        private const string LomentCode_30202 = "Account name duplicate";
        private const string LomentCode_30203 = "Account provider not known / username duplicate";
        private const string LomentCode_30204 = "Account server protocol not known";
        private const string LomentCode_30205 = "Account server SSL Enabled/ Disabled setting invalid";
        private const string LomentCode_30206 = "Account server port setting invalid";
        private const string LomentCode_30207 = "Account status invalid";
        private const string LomentCode_40102 = "Given Payment gateway Id is not available";
        private const string LomentCode_40103 = "Given payment gateway is not active";
        private const string LomentCode_40104 = "Invalid subscription start date";
        private const string LomentCode_40105 = "Invalid subscription end date";
        private const string LomentCode_40106 = "Invalid amount";
        private const string LomentCode_40107 = "Invalid subscribed features";
        private const string LomentCode_40108 = "Given payment id not found";
        private const string LomentCode_40109 = "Given payment status is invalid";
        private const string LomentCode_40110 = "Given log direction is invalid";
        private const string LomentCode_40111 = "Given bill id not found";
        private const string LomentCode_40112 = "Given renewal bill id not found";
        private const string LomentCode_40113 = "Given bill status is invalid";
        private const string LomentCode_40114 = "Given subscription end date for bill is invalid";
        private const string LomentCode_40115 = "Given payment id already has a bill attached to it";
        private const string LomentCode_40116 = "Given bill key is invalid";
        private const string LomentCode_40117 = "Payment failed at payment gateway";
        private const string LomentCode_50202 = "Given subscription id not found";
        private const string LomentCode_50203 = "Given product feature id is invalid";
        private const string LomentCode_50204 = "Given device count is invalid";
        private const string LomentCode_50205 = "Given status is invalid";
        private const string LomentCode_50206 = "Given device's status is invalid";
        private const string LomentCode_50207 = "Device quota exceeded for given bill / subscription";
        private const string LomentCode_50208 = "Given device already has an active subscription for the given product feature";
        private const string LomentCode_50209 = "No subscription/ active subscription found";
        private const string LomentCode_50210 = "Given device already has a subscription/ inactive subscription for the given product feature";
        private const string LomentCode_50211 = "Given primary username not found / inactive";

        private const string LomentCode_99999 = "Client application must be upgraded to continue using Loment services";
        private const string LomentCode_Minus1 = "Unknown response code encountered";

        private ResponseUtil() { }

        static ResponseUtil()
        {
            LoadAndParseMetadata();
        }

        private static void LoadAndParseMetadata()
        {
            MetaMap.Add(-1, new ResponseMeta(-1, LomentCode_Minus1, false));
            MetaMap.Add(0, new ResponseMeta(0, LomentCode_0, true));
            MetaMap.Add(1, new ResponseMeta(1, LomentCode_1, false));
            MetaMap.Add(10002, new ResponseMeta(10002, LomentCode_10002, false));
            MetaMap.Add(10003, new ResponseMeta(10003, LomentCode_10003, false));
            MetaMap.Add(20002, new ResponseMeta(20002, LomentCode_20002, false));
            MetaMap.Add(20003, new ResponseMeta(20003, LomentCode_20003, false));
            MetaMap.Add(30002, new ResponseMeta(30002, LomentCode_30002, false));
            MetaMap.Add(30003, new ResponseMeta(30003, LomentCode_30003, false));
            MetaMap.Add(99999, new ResponseMeta(99999, LomentCode_99999, false));
            MetaMap.Add(10102, new ResponseMeta(10102, LomentCode_10102, false));
            MetaMap.Add(10103, new ResponseMeta(10103, LomentCode_10103, false));
            MetaMap.Add(10104, new ResponseMeta(10104, LomentCode_10104, false));
            MetaMap.Add(10105, new ResponseMeta(10105, LomentCode_10105, false));
            MetaMap.Add(10202, new ResponseMeta(10202, LomentCode_10202, false));
            MetaMap.Add(10203, new ResponseMeta(10203, LomentCode_10203, false));
            MetaMap.Add(10204, new ResponseMeta(10204, LomentCode_10204, false));
            MetaMap.Add(10304, new ResponseMeta(10304, LomentCode_10304, false));
            MetaMap.Add(20102, new ResponseMeta(20102, LomentCode_20102, false));
            MetaMap.Add(20103, new ResponseMeta(20103, LomentCode_20103, false));
            MetaMap.Add(20104, new ResponseMeta(20104, LomentCode_20104, false));
            MetaMap.Add(20105, new ResponseMeta(20105, LomentCode_20105, false));
            MetaMap.Add(20202, new ResponseMeta(20202, LomentCode_20202, false));
            MetaMap.Add(20203, new ResponseMeta(20203, LomentCode_20203, false));
            MetaMap.Add(20204, new ResponseMeta(20204, LomentCode_20204, false));
            MetaMap.Add(20205, new ResponseMeta(20205, LomentCode_20205, false));
            MetaMap.Add(20206, new ResponseMeta(20206, LomentCode_20206, false));
            MetaMap.Add(30102, new ResponseMeta(30102, LomentCode_30102, false));
            MetaMap.Add(30103, new ResponseMeta(30103, LomentCode_30103, false));
            MetaMap.Add(30104, new ResponseMeta(30104, LomentCode_30104, false));
            MetaMap.Add(30105, new ResponseMeta(30105, LomentCode_30105, false));
            MetaMap.Add(30106, new ResponseMeta(30106, LomentCode_30106, false));
            MetaMap.Add(30202, new ResponseMeta(30202, LomentCode_30202, false));
            MetaMap.Add(30203, new ResponseMeta(30203, LomentCode_30203, false));
            MetaMap.Add(30204, new ResponseMeta(30204, LomentCode_30204, false));
            MetaMap.Add(30205, new ResponseMeta(30205, LomentCode_30205, false));
            MetaMap.Add(30206, new ResponseMeta(30206, LomentCode_30206, false));
            MetaMap.Add(30207, new ResponseMeta(30207, LomentCode_30207, false));
            MetaMap.Add(10402, new ResponseMeta(10402, LomentCode_10402, false));
            MetaMap.Add(10403, new ResponseMeta(10403, LomentCode_10403, false));
            MetaMap.Add(40102, new ResponseMeta(40102, LomentCode_40102, false));
            MetaMap.Add(40103, new ResponseMeta(40103, LomentCode_40103, false));
            MetaMap.Add(40104, new ResponseMeta(40104, LomentCode_40104, false));
            MetaMap.Add(40105, new ResponseMeta(40105, LomentCode_40105, false));
            MetaMap.Add(40106, new ResponseMeta(40106, LomentCode_40106, false));
            MetaMap.Add(40107, new ResponseMeta(40107, LomentCode_40107, false));
            MetaMap.Add(40108, new ResponseMeta(40108, LomentCode_40108, false));
            MetaMap.Add(40109, new ResponseMeta(40109, LomentCode_40109, false));
            MetaMap.Add(40110, new ResponseMeta(40110, LomentCode_40110, false));
            MetaMap.Add(40111, new ResponseMeta(40111, LomentCode_40111, false));
            MetaMap.Add(40112, new ResponseMeta(40112, LomentCode_40112, false));
            MetaMap.Add(40113, new ResponseMeta(40113, LomentCode_40113, false));
            MetaMap.Add(40114, new ResponseMeta(40114, LomentCode_40114, false));
            MetaMap.Add(40115, new ResponseMeta(40115, LomentCode_40115, false));
            MetaMap.Add(40116, new ResponseMeta(40116, LomentCode_40116, false));
            MetaMap.Add(40117, new ResponseMeta(40117, LomentCode_40117, false));
            MetaMap.Add(50202, new ResponseMeta(50202, LomentCode_50202, false));
            MetaMap.Add(50203, new ResponseMeta(50203, LomentCode_50203, false));
            MetaMap.Add(50204, new ResponseMeta(50204, LomentCode_50204, false));
            MetaMap.Add(50205, new ResponseMeta(50205, LomentCode_50205, false));
            MetaMap.Add(50206, new ResponseMeta(50206, LomentCode_50206, false));
            MetaMap.Add(50207, new ResponseMeta(50207, LomentCode_50207, false));
            MetaMap.Add(50208, new ResponseMeta(50208, LomentCode_50208, false));
            MetaMap.Add(50209, new ResponseMeta(50209, LomentCode_50209, false));
            MetaMap.Add(50210, new ResponseMeta(50210, LomentCode_50210, false));
            MetaMap.Add(50211, new ResponseMeta(50211, LomentCode_50211, false));
        }

        internal  static ResponseMeta GetResponseMetaForCode(int code)
        {
            return MetaMap[MetaMap.ContainsKey(code) ? code : UnknownCodeKey];
        }
    }
}
