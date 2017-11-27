using Newtonsoft.Json;

namespace Cashew.Internal.Loment.Dto
{
    internal class RegistrationResponse : ResponseDtoBase
    {
        internal int UserId { get; set; }

        public  override string ToString()
        {
            return JsonConvert.SerializeObject(this);
        }
    }
}
