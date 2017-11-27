// ****************************************************************************
// <copyright file="PlatformDetector.cs" company="GalaSoft Laurent Bugnion">
// Copyright © GalaSoft Laurent Bugnion 2009-2016
// </copyright>
// ****************************************************************************
// <author>Laurent Bugnion</author>
// <email>laurent@galasoft.ch</email>
// <date>30.09.2014</date>
// <project>GalaSoft.MvvmLight</project>
// <web>http://www.mvvmlight.net</web>
// <license>
// See license.txt in this solution or http://www.galasoft.ch/license_MIT.txt
// </license>
// ****************************************************************************
using System;

namespace Cashew.Utility.Helper
{
    /// <summary>
    /// A helper class to identify the current platform from a PCL.
    /// </summary>
    public class PlatformDetector
    {

        private static PlatformLibrary? _detectedPlatform;

        public static PlatformLibrary DetectedPlatform
        {
            get
            {
                if (_detectedPlatform == null)
                {
                    _detectedPlatform = GetCurrentPlatform();
                }
                return _detectedPlatform.Value;
            }
        }

        private static PlatformLibrary GetCurrentPlatform()
        {
            // We check Silverlight first because when in the VS designer, the .NET libraries will resolve
            // If we can resolve the SL libs, then we're in SL or WP
            // Then we check .NET because .NET will load the WinRT library (even though it can't really run it)
            // When running in WinRT, it will not load the PresentationFramework lib

            // Check Silverlight
            var dm = Type.GetType("System.ComponentModel.DesignerProperties, System.Windows, Version=2.0.5.0, Culture=neutral, PublicKeyToken=7cec85d7bea7798e");
            if (dm != null)
            {
                return PlatformLibrary.Silverlight;
            }

            // Check .NET 
            var cmdm = Type.GetType("System.ComponentModel.DesignerProperties, PresentationFramework, Version=3.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35");
            if (cmdm != null) // loaded the assembly, could be .net 
            {
                return PlatformLibrary.Net;
            }

            // check WinRT next
            var wadm = Type.GetType("Windows.ApplicationModel.DesignMode, Windows, ContentType=WindowsRuntime");
            if (wadm != null)
            {
                return PlatformLibrary.WinRT;
            }

            return PlatformLibrary.Unknown;
        }

    }

    public enum PlatformLibrary
    {
        Unknown,
        Net,
        WinRT,
        Silverlight
    }
}
