
using System;
using System.Text.RegularExpressions;

using Loment.CashewReference.DB.Base;

namespace Loment.CashewReference.DB.Model
{

    /// <summary>
    /// A helper class for email address validation
    /// </summary>
    public class EmailAddress : BaseObservableObject
    {
        private const int MaxEmailLength = 254;

        private bool _isValidCalculated;
        private bool _isValid;
        private string _email;

        public string Name { get; private set; }

        public string Email
        {
            get { return _email; }

            private set
            {
                if (_email != value)
                {
                    _email = value;

                    _isValidCalculated = false;
                }
            }
        }

        public bool IsValid
        {
            get
            {
                if (_isValidCalculated == false)
                {
                    _isValid = Regex.IsMatch(Email, @"\A(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)\Z",
                        RegexOptions.IgnoreCase);

                    if (_isValid)
                    {
                        _isValid = Email.Length < MaxEmailLength;
                    }

                    _isValidCalculated = true;
                }

                return _isValid;
            }
        }

        public string Description
        {
            get { return string.IsNullOrEmpty(Name) ? Email : string.Format("{0} <{1}>", Name, Email); }
        }

        public string ShortDescription
        {
            get { return string.IsNullOrEmpty(Name) ? Email : Name; }
        }

        public EmailAddress(string email, string name)
        {
            Name = name ?? string.Empty;
            Email = email ?? string.Empty;
        }

        public EmailAddress(string email) : this(email, "")
        {
            _acknlowledgeStatus = Model.AcknlowledgeStatus.None;
        }

        public EmailAddress(EmailAddress address)
        {
            _acknlowledgeStatus = Model.AcknlowledgeStatus.None;
            Name = address.Name;
            Email = address.Email;
        }

        public bool IsSameEmail(EmailAddress emailAddress)
        {
            string emailToCompare = emailAddress.Email ?? string.Empty;

            return String.Compare(Email, emailToCompare, StringComparison.CurrentCultureIgnoreCase) == 0;
        }


        public bool IsSameEmail(string emailAddress)
        {
            return String.Compare(Email, emailAddress, StringComparison.CurrentCultureIgnoreCase) == 0;
        }

        private AcknlowledgeStatus _acknlowledgeStatus;
        public AcknlowledgeStatus AcknlowledgeStatus
        {
            get { return _acknlowledgeStatus; }
            set { Set(ref _acknlowledgeStatus, value); }
        }
    }

    public enum AcknlowledgeStatus
    {
        None,
        Locked,
        Unlocked
    }
}
