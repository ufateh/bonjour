using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public class EmergencyMessage
    {
        public int Id { get; set; }
        public int senderId { get; set; }
        public int receiverId { get; set; }
        public string message { get; set; }
        public string date { get; set; }
        public Boolean isRead { get; set; }
    }
}