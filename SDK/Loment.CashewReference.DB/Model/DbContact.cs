
using System;
using Loment.CashewReference.DB.Base;

namespace Loment.CashewReference.DB.Model
{
    /// <summary>
    /// Represent a Contact in the Database.
    /// </summary>
    public class DbContact : BaseObservableObject
    {

        private string _email = String.Empty;
        private string _name = String.Empty;
        private string _phone = String.Empty;
        private string _imagePath = String.Empty;

        public DbContact()
        {
        }

        public DbContact(DbContact contact)
        {
            SenderId = contact.SenderId;
            Email = contact.Email;
            Name = contact.Name;
            Phone = contact.Phone;
            ImagePath = contact.ImagePath;
            IsCashewAccount = contact.IsCashewAccount;
        }
             
        /// <summary>
        /// SenderId is the ContactId maintained by the Cashew Server
        /// </summary>
        public string SenderId { get; set; }


        private bool _isCashewAccount;
        public bool IsCashewAccount
        {
            get { return _isCashewAccount; }
            set { Set(ref _isCashewAccount, value); }
        }

        /// <summary>
        /// Email address of the DbContact
        /// </summary>
        public string Email
        {
            get { return _email; }
            set { Set(ref _email, value); }
        }

        /// <summary>
        /// Name of the CashewContat
        /// </summary>
        public string Name
        {
            get { return _name; }
            set { Set(ref _name, value); }
        }

        /// <summary>
        /// Phone number of the CashewContat
        /// </summary>
        public string Phone
        {
            get { return _phone; }
            set { Set(ref _phone, value); }
        }

        /// <summary>
        /// This is a cross platform property, and should signify the path to the image,
        /// so each platform can individually load it
        /// </summary>
        public string ImagePath
        {
            get { return _imagePath; }
            set { Set(ref _imagePath, value); }
        }

        public bool HasImage
        {
            get { return string.IsNullOrEmpty(ImagePath) == false; }
        }

        public bool IsFilterMatched(string searchTerm)
        {
            string searchTermLowered = searchTerm.ToLower();
            bool isEmailMatched = (string.IsNullOrEmpty(Email) == false && RemoveDomain(Email).ToLower().Contains(searchTermLowered));
            bool isNameMatched = (string.IsNullOrEmpty(Name) == false && Name.ToLower().Contains(searchTermLowered));
            bool isPhoneMatched = (string.IsNullOrEmpty(Phone) == false && Phone.ToLower().Contains(searchTermLowered));

            return isEmailMatched || isNameMatched || isPhoneMatched;
        }

        private string RemoveDomain(string email)
        {
            string result = email;

            if (email.Contains("@"))
            {
                result = email.Split('@')[0];
            }

            return result;
        }

        public bool IsSameEmail(EmailAddress emailAddress)
        {
            string emailToCompare = emailAddress.Email ?? string.Empty;

            return String.Compare(Email, emailToCompare, StringComparison.CurrentCultureIgnoreCase) == 0;
        }
    }
}
