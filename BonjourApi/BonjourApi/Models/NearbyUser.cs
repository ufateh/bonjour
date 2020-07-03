using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public class NearbyUser
    {
        public int Id { get; set; }
        public Boolean isFriend { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public int friendshipId { get; set; }
        public double latitude { get; set; }
        public double longitude { get; set; }
    }
}