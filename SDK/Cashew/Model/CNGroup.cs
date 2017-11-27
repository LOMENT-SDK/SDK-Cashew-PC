using System;
using System.Collections.Generic;

namespace Cashew.Model
{
    /// <summary>
    /// Represents complete information about a group conversation.
    /// </summary>
    public class CNGroup
    {
        /// <summary>
        /// The conversation this group belongs to.
        /// </summary>
        public string ConverstaionId { get; internal set; }
         
        /// <summary>
        /// The name of the group.
        /// </summary>
        public string Name { get; internal set; }

        /// <summary>
        /// The cashew id of the owner of the group.
        /// </summary>
        public string Owner { get; internal set; }

        /// <summary>
        /// The date/time this group was created.
        /// </summary>
        public DateTime Created { get; internal set; }

        /// <summary>
        /// The date/time this group was last updated.
        /// </summary>
        public DateTime LastUpdate { get; internal set; }

        /// <summary>
        /// The cashew ids of the members of this group.
        /// </summary>
        public List<string> Member { get; internal set; }
    }
}
