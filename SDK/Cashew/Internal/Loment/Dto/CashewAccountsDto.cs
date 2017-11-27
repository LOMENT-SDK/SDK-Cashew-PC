using System.Collections.Generic;

namespace Cashew.Internal.Loment.Dto
{
    internal class CashewAccountsDto : ResponseDtoBase
    {
        public List<CashewAccountDto> device_accounts { get; set; }

        public List<CashewAccountDto> other_accounts { get; set; }
    }
}
