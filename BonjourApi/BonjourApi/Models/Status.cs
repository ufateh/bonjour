using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Web;

namespace bonjourservice.Models
{
    public class Status
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Userstatus { get; set; }
        public string Time { get; set; }
        public string Location { get; set; }
    }
}