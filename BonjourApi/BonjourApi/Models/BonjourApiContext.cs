using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace BonjourApi.Models
{
    public class BonjourApiContext : DbContext
    {
        // You can add custom code to this file. Changes will not be overwritten.
        // 
        // If you want Entity Framework to drop and regenerate your database
        // automatically whenever you change your model schema, please use data migrations.
        // For more information refer to the documentation:
        // http://msdn.microsoft.com/en-us/data/jj591621.aspx
    
        public BonjourApiContext() : base("name=BonjourApiContext")
        {
        }

        public System.Data.Entity.DbSet<bonjourservice.Models.Status> Status { get; set; }

        public System.Data.Entity.DbSet<bonjourservice.Models.User> Users { get; set; }

        public System.Data.Entity.DbSet<BonjourApi.Models.Chat> Chats { get; set; }

        public System.Data.Entity.DbSet<BonjourApi.Models.FriendshipRelation> FriendshipRelations { get; set; }

        public System.Data.Entity.DbSet<BonjourApi.Models.Lift> Lifts { get; set; }

        public System.Data.Entity.DbSet<BonjourApi.Models.EmergencyMessage> EmergencyMessages { get; set; }
    
    }
}
