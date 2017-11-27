using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;

namespace Cashew.Utility.Services
{
    /// <summary>
    /// An interface to interact with a platform specific functionalities.
    /// </summary>
    public interface IPlatformUtility
    {
        /// <summary>
        /// Initializes 
        /// </summary>
        void Init();

        Platform GetPlatform();

        Version GetVersion();

        void RestartApplication();

        /// <summary>
        /// Picks a file using the system file dialog.
        /// </summary>
        /// <param name="filterFormat">Uses the format defined by Microsoft https://msdn.microsoft.com/query/dev12.query?appId=Dev12IDEF1&l=EN-US&k=k(Microsoft.Win32.FileDialog.Filter);k(TargetFrameworkMoniker-.NETFramework,Version%3Dv4.5);k(DevLang-csharp)&rd=true</param>
        /// <returns>The full path of the selected file</returns>
        Task<string> PickFile(string filterFormat = null);

        /// <summary>
        /// A path to the local storage directory
        /// </summary>
        string LocalStorageFullPath { get; }

        /// <summary>
        /// Gives the full path to a relative path
        /// </summary>
        string GetAbsoluteFilePath(string relativePath);

        /// <summary>
        /// Copies a file to the local storage
        /// </summary>
        Task<string> CopyFileToLocalStorage(string sourceFilePath, string subFolder = null, bool isUniqueNameUsed = false, bool shouldReturnAbsolutePath = false);

        /// <summary>
        /// Creates and returns a stream to save an attachment.
        /// </summary>
        Task<Stream> GetAttachmentSaveStream(string accountId, string messageId, string fileName);

        /// <summary>
        /// Creates the relative path for an attachment
        /// </summary>
        Task<string> GetAttachmentFilePath(string accountId, string messageId, string fileName);

        /// <summary>
        /// Deletes all attachments stored for a given email.
        /// </summary>
        /// <param name="accountId">The account Id the email belongs to.</param>
        /// <param name="emailId">The Id of the email.</param>
        Task ClearAttachments(long accountId, long emailId);
        
        /// <summary>
        /// Loads the data for a local file.
        /// </summary>
        Task<byte[]> LoadFileAsync(string relativePath);

        /// <summary>
        /// Creates a folder in the local App storage.
        /// </summary>
        Task<bool> CreateDirectoryLocal(string relativePath);

        /// <summary>
        /// Deletes a file in the local App storage.
        /// </summary>
        Task DeleteFile(string relativePath);

        /// <summary>
        /// Checks if a file exists in the local App storage.
        /// </summary>
        Task<bool> FileExist(string relativePath);

        /// <summary>
        /// Launches a given URL in an external browser
        /// </summary>
        void LaunchUrlInExternalBrowser(string url);

        /// <summary>
        /// Launches a file with a registered external viewer.
        /// </summary>
        void LaunchFileInExternalViewer(string path);

        /// <summary>
        /// Checks if the system is connected to the internet.
        /// </summary>
        /// <returns>A value that indicates if the system is connected to the internet.</returns>
        bool IsConnectedToInternet();

        /// <summary>
        /// Creates a cached version of the file in a sub directory of the file location using the desired name.
        /// </summary>
        /// <param name="originalFilePath">The original path to the file.</param>
        /// <param name="desiredFileName">The desired file name for the cache file.</param>
        /// <returns>The full path to the cache file</returns>
        Task<string> CreateCacheFileCopy(string originalFilePath, string desiredFileName);

        /// <summary>
        /// Creates a cached file in a sub directory of the original file location using the desired name.
        /// </summary>
        /// <param name="originalFilePath">The original path to the file.</param>
        /// <param name="data">The data to write to the file.</param>
        /// <param name="desiredFileName">The desired file name for the cache file.</param>
        /// <returns>The full path to the cache file</returns>
        Task<string> WriteCacheFile(string originalFilePath, byte[] data, string desiredFileName);

        /// <summary>
        /// Copies a file for the source to the target.
        /// </summary>
        /// <param name="sourcePath">The path of the source.</param>
        /// <param name="targetPath">The path of the target.</param>
        /// <returns></returns>
        Task CopyFile(string sourcePath, string targetPath);

        uint GetFileSizeInByte(string filePath);

        List<string> ListFiles(string searchPattern);
    }
}
