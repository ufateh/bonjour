using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public class FriendshipRelation
    {
        public int Id { get; set; }
        public int firstUserId { get; set; }
        public int secondUserId { get; set; }
        public Boolean isFriend { get; set; }

    }
}