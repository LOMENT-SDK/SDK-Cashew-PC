using Cashew.Model;
using Cashew.Model.NetworkResponses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Cashew.Internal.Network
{

    internal abstract class MessageRequestBase
    {
        internal MessageRequestType Type { get; private set; }

        internal object State { get; set; }

        public MessageRequestBase(MessageRequestType type)
        {
            Type = type;
        }
    }

    internal class MessageRequest : MessageRequestBase
    {
        public Task<CashewResponse> Task { get { return TaskCompletionSource.Task; } }

        private TaskCompletionSource<CashewResponse> TaskCompletionSource { get; set; }

        public MessageRequest(MessageRequestType type, int timeOut = 10000)
            : base(type)
        {
            TaskCompletionSource = new TaskCompletionSource<CashewResponse>();
            var ct = new CancellationTokenSource(timeOut);
            ct.Token.Register(() => TaskCompletionSource.TrySetCanceled(), useSynchronizationContext: false);
        }

        public void SetResponse(CashewResponse response)
        {
            TaskCompletionSource.SetResult(response);
        }
    }

    internal class MessageRequest<T> : MessageRequestBase
    {
        public Task<CashewResponse<T>> Task { get { return TaskCompletionSource.Task; } }

        private TaskCompletionSource<CashewResponse<T>> TaskCompletionSource { get; set; }

        public MessageRequest(MessageRequestType type, int timeOut = 10000)
            : base(type)
        {
            TaskCompletionSource = new TaskCompletionSource<CashewResponse<T>>();
            var ct = new CancellationTokenSource(timeOut);
            ct.Token.Register(() => TaskCompletionSource.TrySetCanceled(), useSynchronizationContext: false);
        }

        public void SetResponse(CashewResponse<T> response)
        {
            TaskCompletionSource.TrySetResult(response);
        }
    }
}
