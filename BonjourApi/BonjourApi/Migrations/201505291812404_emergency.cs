namespace BonjourApi.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class emergency : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.EmergencyMessages",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        senderId = c.Int(nullable: false),
                        receiverId = c.Int(nullable: false),
                        message = c.String(),
                        date = c.String(),
                        isRead = c.Boolean(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropTable("dbo.EmergencyMessages");
        }
    }
}
