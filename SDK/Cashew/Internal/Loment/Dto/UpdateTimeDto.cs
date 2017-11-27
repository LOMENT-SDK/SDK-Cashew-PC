using Newtonsoft.Json;

namespace Cashew.Internal.Loment
{
    internal class UpdateTimeDto
    {
        public string User { get; set; }

        [JsonProperty("user_device")]
        public string UserDevice { get; set; }

        [JsonProperty("walnut_email_account")]
        public string WalnutEmailAccount { get; set; }

        [JsonProperty("walnut_email_account_user_device")]
        public string WalnutEmailAccountUserDevice { get; set; }

        [JsonProperty("cashewnut_account")]
        public string CashewAccount { get; set; }

        [JsonProperty("cashewnut_account_user_device")]
        public string CashewAccountUserDevice { get; set; }

        [JsonProperty("peanut_account")]
        public string PeanutAccount { get; set; }

        [JsonProperty("peanut_account_user_device")]
        public string PeanutAccountUserDevice { get; set; }

        public override string ToString()
        {
            return string.Format("User: {0}, UserDevice: {1}, WalnutEmailAccount: {2}, WalnutEmailAccountUserDevice: {3}, CashewAccount: {4}, CashewAccountUserDevice: {5}, PeanutAccount: {6}, PeanutAccountUserDevice: {7}", User, UserDevice, WalnutEmailAccount, WalnutEmailAccountUserDevice, CashewAccount, CashewAccountUserDevice, PeanutAccount, PeanutAccountUserDevice);
        }
    }
}
