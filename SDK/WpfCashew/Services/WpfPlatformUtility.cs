using Cashew.Utility.Helper;
using Cashew.Utility.Services;
using Core;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Threading.Tasks;
using System.Windows.Forms;
using Application = System.Windows.Application;
using OpenFileDialog = Microsoft.Win32.OpenFileDialog;
using SaveFileDialog = Microsoft.Win32.SaveFileDialog;

namespace WpfCashew.Services
{

    /// <summary>
    /// Platform dependent services for things like timers, folder access, file access, etc...
    /// </summary>
    public class WpfPlatformUtility : IPlatformUtility
    {
        public event EventHandler TimeChanged;

        public string LocalStorageFullPath { get; private set; }

        public void Init()
        {
            SystemEvents.TimeChanged += OnSystemEventsTimeChanged;

            LocalStorageFullPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), Constants.Cashew);

            if (!Directory.Exists(LocalStorageFullPath))
                Directory.CreateDirectory(LocalStorageFullPath);

#if DEBUG
            Debug.WriteLine("Storage path: " + LocalStorageFullPath);
#endif
        }


        private void OnSystemEventsTimeChanged(object sender, EventArgs e)
        {
            CultureInfo.CurrentCulture.ClearCachedData();
            EventHelper.NotifyEvent(this, TimeChanged);
        }

        /// <summary>
        /// Returns the platform/OS the application is implemented on
        /// </summary>
        /// <returns></returns>
        public Platform GetPlatform()
        {
            return Platform.WPF;
        }

        /// <summary>
        /// Returns the version of the app assembly
        /// </summary>
        /// <returns></returns>
        public Version GetVersion()
        {
            return Assembly.GetExecutingAssembly().GetName().Version;
        }

        public void RestartApplication()
        {
            Process.Start(Application.ResourceAssembly.Location);
            Application.Current.Shutdown();
        }

        public Task<string> PickFolder()
        {
            string selectedPath = null;
            FolderBrowserDialog folderBrowserDialog = new FolderBrowserDialog();

            if (folderBrowserDialog.ShowDialog() == DialogResult.OK)
            {
                selectedPath = folderBrowserDialog.SelectedPath;
            }

            return Task.FromResult(selectedPath);
        }


        public Task<string> PickFile(string filterFormat = null)
        {
            string pickedFileName = null;
            var openFileDialog = new OpenFileDialog { Filter = filterFormat };

            var result = openFileDialog.ShowDialog();

            if (result.HasValue && result.Value)
            {
                pickedFileName = openFileDialog.FileName;
            }

            return Task.FromResult(pickedFileName);
        }

        public Task<bool> SaveFileAs(string sourceFilePath)
        {
            var saveFileDialog = new SaveFileDialog
            {
                InitialDirectory = Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments),
                FileName = Path.GetFileName(sourceFilePath),
                //Filter = string.Format("{0}|*.*", AppResource.SaveFilter_All)
                Filter = string.Format("{0}|*.*", "Save All")
            };


            var result = saveFileDialog.ShowDialog();
            bool isSuccess = true;

            if (result.HasValue && result.Value)
            {
                try
                {
                    File.Copy(sourceFilePath, saveFileDialog.FileName, true);
                }
                catch (Exception)
                {
                    isSuccess = false;
                }
            }

            return Task.FromResult(isSuccess);
        }

        public Task<bool> SaveFile(string sourceFilePath, string destinationPath)
        {
            bool isSuccess = true;

            try
            {
                File.Copy(sourceFilePath, Path.Combine(destinationPath, Path.GetFileName(sourceFilePath)), true);
            }
            catch (Exception)
            {
                isSuccess = false;
            }

            return Task.FromResult(isSuccess);
        }


        public async Task<string> CopyFileToLocalStorage(string sourceFilePath, string subFolderDirectory = null, bool isUniqueNameUsed = false, bool shouldReturnAbsolutePath = false)
        {
            string fileName;

            // We want to maintain file extensions
            if (isUniqueNameUsed)
            {
                var ext = Path.GetExtension(sourceFilePath);
                var name = Path.GetFileNameWithoutExtension(Path.GetTempFileName());
                fileName = string.Format("{0}{1}", name, ext);
            }
            else
            {
                fileName = Path.GetFileName(sourceFilePath);
            }

            var destinationFullPath = subFolderDirectory != null
                ? Path.Combine(LocalStorageFullPath, subFolderDirectory, fileName)
                : Path.Combine(LocalStorageFullPath, fileName);

            await CreateDirectoryLocal(Path.GetDirectoryName(destinationFullPath));

            File.Copy(sourceFilePath, destinationFullPath, true);

            return shouldReturnAbsolutePath ? destinationFullPath : fileName;
        }

        public Task<bool> CreateDirectoryLocal(string relativePath)
        {
            return Task.Run(() =>
            {
                try
                {
                    var fullPath = Path.Combine(LocalStorageFullPath, relativePath);
                    if (Directory.Exists(fullPath) == false)
                    {
                        Directory.CreateDirectory(fullPath);
                    }
                    return true;
                }
                catch
                {
                    return false;
                }
            });
        }

        public void LaunchUrlInExternalBrowser(string url)
        {
            var startInfo = new ProcessStartInfo("IExplore.exe") { UseShellExecute = true, Arguments = url };

            Process.Start(startInfo);
        }

        public void LaunchFileInExternalViewer(string path)
        {
            var startInfo = new ProcessStartInfo(path) { UseShellExecute = true };

            Process.Start(startInfo);
        }


        public bool IsConnectedToInternet()
        {
            try
            {
                using (var client = new WebClient())
                using (client.OpenRead("http://www.google.com"))
                {
                    return true;
                }
            }
            catch
            {
                return false;
            }
        }

        public Task DeleteFile(string relativePath)
        {
            return Task.Run(() =>
            {
                var fullPath = Path.Combine(LocalStorageFullPath, relativePath);
                if (File.Exists(fullPath))
                {
                    File.Delete(fullPath);
                }
            });
        }

        public async Task<Stream> GetAttachmentSaveStream(string accountId, string messageId, string fileName)
        {
            var realtiveFilePath = await GetAttachmentFilePath(accountId, messageId, fileName);
            var filePath = Path.Combine(LocalStorageFullPath, realtiveFilePath);
            if (File.Exists(filePath))
                File.Delete(filePath);
            return File.OpenWrite(filePath);
        }

        public async Task<string> GetAttachmentFilePath(string accountId, string messageId, string fileName)
        {
            var filePathSaveFileName = fileName;
            foreach (var item in Path.GetInvalidFileNameChars())
            {
                filePathSaveFileName = filePathSaveFileName.Replace(item, '_');
            }

            var emailAttachmentsFolderPath = Path.Combine("Attachments", accountId.ToString(), messageId);
            var folderAvailable = await CreateDirectoryLocal(emailAttachmentsFolderPath);
            if (folderAvailable)
            {
                var filePath = Path.Combine(emailAttachmentsFolderPath, filePathSaveFileName);
                return filePath;
            }
            Exception ex = new InvalidOperationException("Failed to provide folder: " + emailAttachmentsFolderPath);
            //Log.ErrorForContext<WpfPlatformUtility>(ex, "We couldn't get the folder needed to save attachment for {@emailID}", emailId);
            throw ex;
        }

        public Task<bool> FileExist(string relativePath)
        {
            return Task.Run(() =>
            {
                var filePath = Path.Combine(LocalStorageFullPath, relativePath);
                return File.Exists(filePath);
            });
        }

        public Task<byte[]> LoadFileAsync(string relativePath)
        {
            return Task.Run(() =>
            {
                var bytes = File.ReadAllBytes(GetAbsoluteFilePath(relativePath));
                return bytes;
            });
        }

        public string GetAbsoluteFilePath(string relativePath)
        {
            return Path.Combine(LocalStorageFullPath, relativePath);
        }

        public Task<string> CreateCacheFileCopy(string originalFilePath, string desiredFileName)
        {
            return Task.Run(() =>
            {
                var absolutPath = GetAbsoluteFilePath(originalFilePath);
                var fi = new FileInfo(absolutPath);

                var fileCacheDir = Path.Combine(fi.Directory.FullName, "~" + fi.Name);
                var cacheFilePath = Path.Combine(fileCacheDir, desiredFileName);
                if (File.Exists(cacheFilePath))
                    return cacheFilePath;

                var di = Directory.CreateDirectory(fileCacheDir);
                di.Attributes = di.Attributes | FileAttributes.Hidden;

                File.Copy(absolutPath, cacheFilePath, true);
                var fic = new FileInfo(cacheFilePath);

                return cacheFilePath;
            });
        }

        public Task<string> WriteCacheFile(string originalFilePath, byte[] data, string desiredFileName)
        {
            return Task.Run(() =>
            {
                var absolutPath = GetAbsoluteFilePath(originalFilePath);
                var fi = new FileInfo(absolutPath);

                var fileCacheDir = Path.Combine(fi.Directory.FullName, "~" + fi.Name);
                var cacheFilePath = Path.Combine(fileCacheDir, desiredFileName);
                if (File.Exists(cacheFilePath))
                    return cacheFilePath;

                var di = Directory.CreateDirectory(fileCacheDir);
                di.Attributes = di.Attributes | FileAttributes.Hidden;

                File.WriteAllBytes(cacheFilePath, data);
                var fic = new FileInfo(cacheFilePath);

                return cacheFilePath;
            });
        }

        public async Task<Stream> GetAttachmentSaveStream(int emailId, string attachmentFileName)
        {
            var emailAttachmentsFolderPath = Path.Combine("Attachments", emailId.ToString());
            var folderAvailable = await CreateDirectoryLocal(emailAttachmentsFolderPath);
            if (folderAvailable)
            {
                var filePath = Path.Combine(LocalStorageFullPath, emailAttachmentsFolderPath, attachmentFileName);
                var stream = File.Open(filePath, FileMode.Create, FileAccess.Write);
                return stream;
            }
            Exception ex = new InvalidOperationException("Failed to provide folder: " + emailAttachmentsFolderPath);
            //Log.ErrorForContext<WpfPlatformUtility>(ex, "We couldn't get the folder needed to save attachment for {@emailID}", emailId);
            throw ex;
        }

        public Task CopyFile(string sourcePath, string targetPath)
        {
            return Task.Run(() =>
            {
                File.Copy(sourcePath, targetPath);
            });
        }

        public Task ClearAttachments(long accountId, long emailId)
        {
            return Task.Run(() =>
            {
                var emailAttachmentsFolderPath = Path.Combine("Attachments", accountId.ToString(), emailId.ToString());
                emailAttachmentsFolderPath = Path.Combine(LocalStorageFullPath, emailAttachmentsFolderPath);

                if (Directory.Exists(emailAttachmentsFolderPath))
                {
                    Directory.Delete(emailAttachmentsFolderPath, true);
                }
            });
        }


        public uint GetFileSizeInByte(string filePath)
        {
            if (File.Exists(filePath))
            {
                FileInfo fi = new FileInfo(filePath);
                return (uint)fi.Length;
            }
            else
            {
                return 0;
            }
        }

        public List<string> ListFiles(string searchPattern)
        {
            List<string> ret = new List<string>();
            if (Directory.Exists(LocalStorageFullPath))
            {
                var di = new DirectoryInfo(LocalStorageFullPath);
                ret.AddRange(di.GetFiles(searchPattern).Select(x => x.Name));
            }
            return ret;
        }

        Cashew.Utility.Services.Platform IPlatformUtility.GetPlatform()
        {
            return Platform.WPF;
        }
    }
}