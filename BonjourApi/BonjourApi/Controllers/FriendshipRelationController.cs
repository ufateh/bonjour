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

namespace BonjourApi.Controllers
{
    public class FriendshipRelationController : ApiController
    {
        private BonjourApiContext db = new BonjourApiContext();

        
        // GET: api/FriendshipRelation/5
        [ResponseType(typeof(FriendshipRelation))]
        public async Task<IHttpActionResult> GetFriendshipRelation(int id)
        {
            FriendshipRelation friendshipRelation = await db.FriendshipRelations.FindAsync(id);
            if (friendshipRelation == null)
            {
                return NotFound();
            }

            return Ok(friendshipRelation);
        }

        [Route("api/FriendshipRelation/GetFriends/{id}/{isFriend}")]
        public HttpResponseMessage GetFriendRequests(int id,bool isFriend)
        {
            List<FriendRequest> list = new List<FriendRequest>();
            if(isFriend==false)
            {
                foreach (FriendshipRelation r in db.FriendshipRelations)
                {
                    if (r.secondUserId == id && r.isFriend == false)
                    {
                        FriendRequest request = new FriendRequest();
                        request.userId = r.firstUserId;
                        request.friendshipId = r.Id;
                        list.Add(request);
                    }
                }
                foreach (FriendRequest rr in list)
                {
                    rr.name = db.Users.FirstOrDefault(u => u.Id == rr.userId).name;
                    rr.email = db.Users.FirstOrDefault(u => u.Id == rr.userId).email;
                }
            }
            if (isFriend == true)
            {
                foreach (FriendshipRelation r in db.FriendshipRelations)
                {
                    if (r.isFriend == true && r.secondUserId == id || r.firstUserId == id && r.isFriend == true)
                    {
                        FriendRequest request = new FriendRequest();
                        if(r.secondUserId==id)
                            request.userId = r.firstUserId;
                        else
                            request.userId = r.secondUserId;
                        
                        request.friendshipId = r.Id;
                        list.Add(request);
                    }
                }
                foreach (FriendRequest rr in list)
                {
                    rr.name = db.Users.FirstOrDefault(u => u.Id == rr.userId).name;
                    rr.email = db.Users.FirstOrDefault(u => u.Id == rr.userId).email;
                }
            }
            
            HttpResponseMessage response;
            if(list.Count>0)
            {
                response = Request.CreateResponse(HttpStatusCode.OK, list);
            }
            else
            {
                response = Request.CreateResponse(HttpStatusCode.NoContent, list);
            }
            return response;

        }

        // PUT: api/FriendshipRelation/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutFriendshipRelation(int id)
        {
            FriendshipRelation relation = db.FriendshipRelations.FirstOrDefault(f=> f.Id==id);
            relation.isFriend = true;
            db.Entry(relation).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FriendshipRelationExists(id))
                {
                    return StatusCode(HttpStatusCode.NoContent);
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.OK);
        }

        // POST: api/FriendshipRelation
        [ResponseType(typeof(FriendshipRelation))]
        public async Task<IHttpActionResult> PostFriendshipRelation([FromBody]FriendshipRelation friendshipRelation)
        {
            friendshipRelation.isFriend = false;
            if(db.FriendshipRelations.FirstOrDefault(r=> r.firstUserId==friendshipRelation.firstUserId && r.secondUserId==friendshipRelation.secondUserId)==null)
            {
                db.FriendshipRelations.Add(friendshipRelation);
                await db.SaveChangesAsync();

                return CreatedAtRoute("DefaultApi", new { id = friendshipRelation.Id }, friendshipRelation);
            }
            else
            {
                return CreatedAtRoute("DefaultApi", new { id = 0 }, new FriendshipRelation());
            }
            
        }

        // DELETE: api/FriendshipRelation/5
        [ResponseType(typeof(FriendshipRelation))]
        public async Task<IHttpActionResult> DeleteFriendshipRelation(int id)
        {
            FriendshipRelation friendshipRelation = await db.FriendshipRelations.FindAsync(id);
            if (friendshipRelation == null)
            {
                return NotFound();
            }

            db.FriendshipRelations.Remove(friendshipRelation);
            await db.SaveChangesAsync();

            return Ok(friendshipRelation);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool FriendshipRelationExists(int id)
        {
            return db.FriendshipRelations.Count(e => e.Id == id) > 0;
        }
    }
}