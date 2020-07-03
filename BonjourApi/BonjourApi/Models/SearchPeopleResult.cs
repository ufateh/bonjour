using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public class SearchPeopleResult
    {
        public int Id { get; set; }
        public Boolean isFriend { get; set; }
        public string Name { get; set; }
    }
}