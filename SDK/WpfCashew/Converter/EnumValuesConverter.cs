using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace WpfCashew.Converter
{
    public class EnumValuesConverter : IValueConverter
    {
        /// <summary>
        /// Caches the "enum objects" for the lifetime of the application.
        /// </summary>
        internal static class EnumValueCache
        {
            private static readonly IDictionary<Type, object[]> Cache = new Dictionary<Type, object[]>();

            public static object[] GetValues(Type type)
            {
                if (!type.IsEnum)
                    throw new ArgumentException("Type '" + type.Name + "' is not an enum");

                object[] values;
                if (!Cache.TryGetValue(type, out values))
                {
                    values = type.GetFields()
                        .Where(f => f.IsLiteral)
                        .Select(f => f.GetValue(null))
                        .ToArray();
                    Cache[type] = values;
                }
                return values;
            }
        }

        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value == null)
                return null;
            else
                return EnumValueCache.GetValues(value.GetType());
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
