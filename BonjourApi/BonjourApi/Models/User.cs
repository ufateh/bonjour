using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Web;

namespace bonjourservice.Models
{
    public class User
    {
        public int Id { get; set; }
        public String name { get; set; }
        public String email { get; set; }
        public String password { get; set; }
        public int age { get; set; }
        public String city { get; set; }
        public int visibility { get; set; }
        public int isOnline { get; set; }
        public double latitude { get; set; }
        public double longitude { get; set; }
        public String gender { get; set; }

    }
}