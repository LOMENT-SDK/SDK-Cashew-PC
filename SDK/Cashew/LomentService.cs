using Cashew.Internal.Loment;
using Cashew.Internal.Loment.Dto;
using Cashew.Internal.Loment.Extensions;
using Cashew.Model;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;

namespace Cashew
{
    /// <summary>
    /// A service that provides access to Loment's account management service.
    /// </summary>
    public class LomentService
    {
        private const int SUBSCRIPTION_FEATURE = 3;
        private LomentApi _lomentApi;

        /// <summary>
        /// The ctor of the LomentService.
        /// </summary>
        /// <param name="clientId">A client Id provided by Loment and used to communicate with their API.</param>
        /// <param name="partnerId">A partner Id provided by Loment and used to communicate with their API.</param>
        public LomentService(string clientId, string partnerId)
        {
            CheckSdkInit();

            if (string.IsNullOrEmpty(clientId))
                throw new ArgumentException("No client Id provided.");

            if (string.IsNullOrEmpty(partnerId))
                throw new ArgumentException("No partner Id provided.");

            Sdk.ClientId = clientId;
            Sdk.PartnerId = partnerId;

            _lomentApi = new LomentApi();
        }

        /// <summary>
        /// Registers a new Loment account.
        /// </summary>
        /// <param name="fullName">The full name of teh account owner</param>
        /// <param name="password">A password for the account.</param>
        /// <param name="email">The email address used to log in to the account.</param>
        /// <param name="phone">The phone number of the user.</param>
        /// <param name="countryCode">A 2 character country code of residence.</param>
        /// <returns></returns>
        public async Task<CNLomentAccount> RegisterAsync(string fullName, string password, string email, string phone, string countryCode)
        {
            var json = await _lomentApi.RegisterUserAccount(fullName, password, email, phone, countryCode);

            var account = json.SafeDeserializeObject<LomentAccountDto>();

            if (account.ResponseMeta.IsSuccess)
            {
                return new CNLomentAccount
                {
                    Email = email,
                    UserId = account.UserId.ToString(),
                    Password = password,
                    Phone = account.PrimaryMobileNumber,
                    UserName = email
                };
            }
            else
            {
                throw new ApiException(account.ResponseMeta);
            }
        }



        /// <summary>
        /// Authenticates a Loment account.
        /// </summary>
        /// <param name="userName">The user name of the Loment account. </param>
        /// <param name="password">The password for the account.</param>
        /// <returns>A Loment account.</returns>
        public async Task<CNLomentAccount> AuthenticateAsync(string userName, string password)
        {
            var json = await _lomentApi.AuthenticateUserAccount(userName, password);

            var account = json.SafeDeserializeObject<LomentAccountDto>();

            if (account.ResponseMeta.IsSuccess)
            {
                return new CNLomentAccount
                {
                    Email = userName,
                    UserId = account.UserId.ToString(),
                    Password = password,
                    Phone = account.PrimaryMobileNumber,
                    UserName = userName
                };
            }
            else
            {
                throw new ApiException(account.ResponseMeta);
            }
        }

        /// <summary>
        /// Verifies the subscription of a Loment account.
        /// </summary>
        /// <param name="lomentAccount">The Loment account to verify.</param>
        /// <returns>Returns a boolean that indicates whether the subscription is valid or not.</returns>
        public async Task<bool> VerifySubscriptionAsync(CNLomentAccount lomentAccount)
        {
            var json = await _lomentApi.GetFeatureDeviceSubscription(lomentAccount.UserName, SUBSCRIPTION_FEATURE, Sdk.DeviceInformationProvider.GetUniqueDeviceId());

            var subscription = json.SafeDeserializeObject<SubscriptionDto>();

            if (subscription.ResponseMeta.IsSuccess)
            {
                return subscription.subscription_details.status == "1";
            }
            else
            {
                throw new ApiException(subscription.ResponseMeta);
            }
        }

        /// <summary>
        /// Gets all Cashew accounts associated with a Loment account.
        /// </summary>
        /// <param name="lomentAccount">THe Loment account.</param>
        /// <returns>A list of Cahsew accounts.</returns>
        public async Task<IEnumerable<CNCashewAccount>> GetCashewAccountsAsync(CNLomentAccount lomentAccount)
        {
            List<CNCashewAccount> cashewAccounts = new List<CNCashewAccount>();

            var json = await _lomentApi.GetCashewAccounts(lomentAccount.UserName, Sdk.DeviceInformationProvider.GetUniqueDeviceId());

            var dto = json.SafeDeserializeObject<CashewAccountsDto>();

            if (dto.ResponseMeta.IsSuccess)
            {
                if (dto.device_accounts != null)
                {
                    cashewAccounts.AddRange(dto.device_accounts.Select(x => new CNCashewAccount
                    {
                        LomentAccountUserId = x.id,
                        UserName = x.username,
                        Status = x.status,
                        CreateDateUtc = DateTime.Parse(x.creation_date, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal).ToUniversalTime(),
                        LastUpdateDateUtc = DateTime.Parse(x.last_update_date, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal).ToUniversalTime()
                    }));
                }

                if (dto.other_accounts != null)
                {
                    cashewAccounts.AddRange(dto.other_accounts.Select(x => new CNCashewAccount
                    {
                        LomentAccountUserId = x.id,
                        UserName = x.username,
                        Status = x.status,
                        CreateDateUtc = DateTime.Parse(x.creation_date, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal).ToUniversalTime(),
                        LastUpdateDateUtc = DateTime.Parse(x.last_update_date, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal).ToUniversalTime()
                    }));
                }

                return cashewAccounts;
            }
            else
            {
                throw new ApiException(dto.ResponseMeta);
            }
        }

        /// <summary>
        /// Creates a new Cashew account under the given Loment account.
        /// </summary>
        /// <param name="lomentAccount">The Loment account that will own the Cashew account.</param>
        /// <param name="accountName">A name for the Cashew account to create.</param>
        /// <returns></returns>
        public async Task<CNCashewAccount> CreateCashewAcountAsync(CNLomentAccount lomentAccount, string accountName)
        {
            var json = await _lomentApi.CreateCashewAccount(lomentAccount.UserName, accountName, Sdk.DeviceInformationProvider.GetUniqueDeviceId());

            var dto = json.SafeDeserializeObject<CashewAccountCreateDto>();
            if (dto.ResponseMeta.IsSuccess)
            {
                var cashewAccount = new CNCashewAccount
                {
                    LomentAccountUserId = dto.account_id,
                    UserName = accountName,
                    Status = "Unknown",
                    CreateDateUtc = DateTime.UtcNow,
                    LastUpdateDateUtc = DateTime.UtcNow
                };

                return cashewAccount;
            }
            else
            {
                throw new ApiException(dto.ResponseMeta);
            }
        }

        /// <summary>
        /// Registers the current device for a Loment account.
        /// </summary>
        /// <param name="lomentAccount">The Loment account to register the device for.</param>
        /// <param name="deviceName">An optional name for teh device.</param>
        /// <returns></returns>
        public async Task CreateDeviceAsync(CNLomentAccount lomentAccount, string deviceName = null)
        {
            var json = await _lomentApi.CreateDevice(lomentAccount.UserName
                , Sdk.DeviceInformationProvider.GetUniqueDeviceId()
                , ((int)Sdk.DeviceInformationProvider.GetDeviceType()).ToString()
                , Sdk.DeviceInformationProvider.GetOsName()
                , deviceName);

            var dto = json.SafeDeserializeObject<ResponseDtoBase>();

            if (!dto.ResponseMeta.IsSuccess)
            {
                throw new ApiException(dto.ResponseMeta);
            }
        }

        /// <summary>
        /// Starts a password recovery process.
        /// </summary>
        /// <param name="email">The email of the Loment account.</param>
        /// <returns></returns>
        public async Task RecoverPasswordAsync(string email)
        {
            var json = await _lomentApi.BeginUserAccountRecovery(email);

            var dto = json.SafeDeserializeObject<ResponseDtoBase>();

            if (dto.ResponseMeta.IsSuccess)
            {

            }
            else
            {
                throw new ApiException(dto.ResponseMeta);
            }

        }


        /// <summary>
        /// Looks up Loment accounts and associated Cashew accounts.
        /// </summary>
        /// <param name="lomentAccount">The Loment account that looks up.</param>
        /// <param name="phoneNumbers">A list of phone numbers that potentially belong to a Loment account.</param>
        /// <param name="emailAddresses">A list of email addresses that potentially belong to a Loment account.</param>
        /// <returns>A list of friend suggestions that either match a given phone number or email.</returns>
        public async Task<List<CNFriendSuggestion>> GetCashewFriendSuggestions(CNLomentAccount lomentAccount, List<string> phoneNumbers, List<string> emailAddresses)
        {
            List<CNFriendSuggestion> friendSuggestions = new List<CNFriendSuggestion>();

            var json = await _lomentApi.GetFriendSuggestions(lomentAccount.Email, lomentAccount.Password, phoneNumbers, emailAddresses);

            var dto = json.SafeDeserializeObject<ResponseDtoBase>();

            if (!dto.ResponseMeta.IsSuccess)
            {
                throw new ApiException(dto.ResponseMeta);
            }

            try
            {
                JObject jo = (JObject)Newtonsoft.Json.JsonConvert.DeserializeObject(json);

                var phoneMatches = jo.Descendants().FirstOrDefault(x => x.Path == "contacts.phone");
                var emailMatches = jo.Descendants().FirstOrDefault(x => x.Path == "contacts.email");

                foreach (var phoneHit in phoneMatches.First.Children())
                {
                    var phone = ((JProperty)phoneHit).Name;
                    var cashewAccountIds = new List<string>();
                    string name = "";

                    foreach (var cashewAccount in phoneHit.First.Children())
                    {
                        cashewAccountIds.Add(((JProperty)cashewAccount).Name);
                        name = cashewAccount.First.Value<string>();
                    }
                    CNFriendSuggestion oc = new CNFriendSuggestion(name, "", phone, cashewAccountIds);

                    friendSuggestions.Add(oc);
                }

                foreach (var emailHit in emailMatches.First.Children())
                {
                    var email = ((JProperty)emailHit).Name;

                    var cashewAccountIds = new List<string>();
                    string name = "";
                    foreach (var cashewAccount in emailHit.First.Children())
                    {
                        cashewAccountIds.Add(((JProperty)cashewAccount).Name);
                        name = cashewAccount.First.Value<string>();
                    }

                    CNFriendSuggestion oc = new CNFriendSuggestion(name, email, "", cashewAccountIds);

                    friendSuggestions.Add(oc);
                }

            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Parsing of friend suggestions failed.", ex);
            }

            return friendSuggestions;
        }

        private static void CheckSdkInit()
        {
            if (Sdk.IsInitialized == false)
            {
                throw new InvalidOperationException("The SDK is not initialized. Please call Sdk.Initialize() first.");
            }
        }

    }
}
