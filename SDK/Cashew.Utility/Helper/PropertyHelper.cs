using System;
using System.Linq.Expressions;

namespace Cashew.Utility.Helper
{
    /// <summary>
    /// A helper class to retrieve safely the name of a property.
    /// </summary>
    public class PropertyHelper
    {
        public static string GetPropertyName<T>(Expression<Func<T>> exp)
        {
            return (((MemberExpression)(exp.Body)).Member).Name;
        }
    }
}
