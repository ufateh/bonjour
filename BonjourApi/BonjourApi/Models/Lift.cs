using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public class Lift
    {
        public int Id { get; set; }
        public string  name { get; set; }
        public string  destination { get; set; }
        public string vacancy { get; set; }
        public string contactNo { get; set; }
        public string time { get; set; }
        public string slat { get; set; }
        public string slon { get; set; }
        public string elat { get; set; }
        public string elon { get; set; }
    }
}