using System;

namespace Cashew.Utiltity.Extensions
{
    /// <summary>
    /// Extension methods for DateTime
    /// </summary>
    public static class DateTimeExtensions
    {
        /// <summary>
        /// Calculates the Java time.
        /// </summary>
        public static long  JavaTimeMillis(this DateTime d)
        {
            DateTime Jan1970 = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
            TimeSpan javaSpan = d - Jan1970;
            return (long)javaSpan.TotalMilliseconds;
        }
    }
}
