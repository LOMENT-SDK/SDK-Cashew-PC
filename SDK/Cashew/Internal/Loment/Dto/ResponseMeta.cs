using System.Collections.Generic;

namespace Cashew.Internal.Loment.Dto
{
    internal class ResponseMetaWrapper { public List<ResponseMeta> Metas { get; set; } }

    public class ResponseMeta
    {
        public int Code { get; private set; }

        public string Message { get; private set; }

        public  bool IsSuccess { get; private set; }

        public ResponseMeta(int code, string message, bool isSuccess)
        {
            Code = code;
            Message = message;
            IsSuccess = isSuccess;
        }

        public override string ToString()
        {
            return string.Format("Code: {0}, Message: {1}, IsSuccess: {2}", Code, Message, IsSuccess);
        }
    }
}
