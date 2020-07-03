using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public static class Utility
    {
        public static double distFrom(double lat1, double lng1, double lat2, double lng2)
        {
            double earthRadius = 6371; //kilometersS
            
            double dLat = DegreeToRadian(lat2 - lat1);
            double dLng = DegreeToRadian(lng2 - lng1);
            
            double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                       Math.Cos(DegreeToRadian(lat1)) * Math.Cos(DegreeToRadian(lat2)) *
                       Math.Sin(dLng / 2) * Math.Sin(dLng / 2);
           
            double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));
            double dist = (double)(earthRadius * c);
            return dist;
            //return dist * 1000;

        }
        private static double DegreeToRadian(double angle)
        {
            return Math.PI * angle / 180.0;
        }

        internal static int getRadarAera(Radar area)
        {
            if (area.Equals(Radar.NEARBY_PEOPLE_AREA))
                return Constants.NEARBY_PEOPLE_AREA;
            else if (area.Equals(Radar.NEARBY_EMERGENCY_AREA))
                return Constants.NEARBY_EMERGENCY_AREA;
            else if (area.Equals(Radar.NEARBY_FRIENDS_AREA))
                return Constants.NEARBY_FRIENDS_AREA;
            else { return Constants.NEARBY_GENERAL_AREA; }
        }
    }

    public enum Radar
    {
         NEARBY_PEOPLE_AREA,
        NEARBY_EMERGENCY_AREA,
        NEARBY_FRIENDS_AREA
    }
}