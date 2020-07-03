namespace BonjourApi.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class initialcreate : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Lifts",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        name = c.String(),
                        destination = c.String(),
                        time = c.String(),
                        slat = c.String(),
                        slon = c.String(),
                        elat = c.String(),
                        elon = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropTable("dbo.Lifts");
        }
    }
}
