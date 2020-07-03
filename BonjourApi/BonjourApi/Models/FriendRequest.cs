using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public class FriendRequest
    {
        public int userId { get; set; }
        public string name  { get; set; }
        public string email { get; set; }
        public int friendshipId { get; set; }
    }
}