using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using BonjourApi.Models;
using System.Web.Routing;

namespace BonjourApi.Controllers
{
    public class ChatsController : ApiController
    {
        private BonjourApiContext db = new BonjourApiContext();

        [Route("api/Chats/{friendshipId:int}")]
        public HttpResponseMessage GetChat(int friendshipId)
        {
            List<Chat> chats= db.Chats.Where(c => c.friendshipId==friendshipId).ToList<Chat>();
            if(chats==null)
                return Request.CreateResponse(HttpStatusCode.NotFound, chats);
            else if(chats.Count==0)
                return Request.CreateResponse(HttpStatusCode.NotFound, chats);
            else
                return Request.CreateResponse(HttpStatusCode.OK, chats);
            
        }

        // POST: api/Chats
        [ResponseType(typeof(Chat))]
        public async Task<IHttpActionResult> PostChat([FromBody]Chat chat)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Chats.Add(chat);
            await db.SaveChangesAsync();
            return Ok(chat);
            
        }

        // DELETE: api/Chats/5
        [ResponseType(typeof(Chat))]
        public async Task<IHttpActionResult> DeleteChat(int id)
        {
            Chat chat = await db.Chats.FindAsync(id);
            if (chat == null)
            {
                return NotFound();
            }

            db.Chats.Remove(chat);
            await db.SaveChangesAsync();

            return Ok(chat);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ChatExists(int id)
        {
            return db.Chats.Count(e => e.Id == id) > 0;
        }
    }
}