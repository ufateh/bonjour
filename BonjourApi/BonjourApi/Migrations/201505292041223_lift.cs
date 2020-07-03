namespace BonjourApi.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class lift : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Lifts", "vacancy", c => c.String());
            AddColumn("dbo.Lifts", "contactNo", c => c.String());
        }
        
        public override void Down()
        {
            DropColumn("dbo.Lifts", "contactNo");
            DropColumn("dbo.Lifts", "vacancy");
        }
    }
}
