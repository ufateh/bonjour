using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using BonjourApi.Models;
using bonjourservice.Models;
using System.Threading.Tasks;

namespace BonjourApi.Controllers
{
    public class UserController : ApiController
    {
        private BonjourApiContext db = new BonjourApiContext();

        // GET: api/User
        public IQueryable<User> GetUsers()
        {
            return db.Users;
        }

        [HttpGet]
        [Route("api/User/NearbyAll/{id:int}")]
        public HttpResponseMessage GetNearbyUsers(int id)
        {
            if (!UserExists(id))
            {
                return null;
            }
            else
            {
                int distance = 5;//Utility.getRadarAera(area);
                List<NearbyUser> list = new List<NearbyUser>();
                User user = db.Users.FirstOrDefault(u => u.Id == id);

                foreach (User u in db.Users)
                {
                    if (u.Id != id)
                    {
                        if (Utility.distFrom(user.latitude, user.longitude, u.latitude, u.longitude) <= distance)
                        {
                            NearbyUser temp = new NearbyUser();
                            temp.Id = u.Id;
                            temp.Name = u.name;
                            temp.Email = u.email;
                            temp.latitude = u.latitude;
                            temp.longitude = u.longitude;
                            temp.isFriend=false;
                            list.Add(temp);
                        }
                    }
                }
                foreach(NearbyUser u in list)
                {
                    FriendshipRelation relation = db.FriendshipRelations.FirstOrDefault(r => r.firstUserId == id && r.secondUserId == u.Id || r.firstUserId == u.Id && r.secondUserId == id);
                    if (relation != null)
                    {
                        u.isFriend = relation.isFriend;
                        u.friendshipId = relation.Id;
                    }
                    
                }
                    return Request.CreateResponse(HttpStatusCode.OK, list);
            }
        }


        [Route("api/User/Validate")]
        [ResponseType(typeof(User))]
        public IHttpActionResult PostValidateUser([FromBody] User user)
        {
            User authenticatedUser = db.Users.FirstOrDefault<User>(u => u.email.Equals(user.email) && u.password.Equals(user.password));
            if (authenticatedUser != null)
            {
                user.isOnline = 1;
                db.Entry(user).State = EntityState.Modified;

                try
                {
                    db.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                }
                return Ok(authenticatedUser);
            }

            else
                return NotFound();
        }

        // GET: api/User/5
        [ResponseType(typeof(User))]
        public IHttpActionResult GetUser(int id)
        {
            User user = db.Users.Find(id);
            if (user == null)
            {
                return NotFound();
            }

            return Ok(user);
        }

        // PUT: api/User/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutUser(int id, [FromBody] User user)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != user.Id)
            {
                return BadRequest();
            }

            db.Entry(user).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        [HttpPut]
        [Route("api/User/Offline/{id}")]
        [ResponseType(typeof(void))]
        public IHttpActionResult SetUserOffline(int id)
        {
            if (!UserExists(id))
            {
                return NotFound();
            }

            User user = db.Users.FirstOrDefault(u => u.Id == id);
            user.isOnline = 0;
            db.Entry(user).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                throw;
            }
            return StatusCode(HttpStatusCode.OK);
        }

        // POST: api/User
        [ResponseType(typeof(User))]
        public IHttpActionResult PostUser([FromBody] User user)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            if (db.Users.FirstOrDefault(u => u.email == user.email) != null)
                return BadRequest("Provided email already registeted!");
            else
                db.Users.Add(user);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = user.Id }, user);
        }

        // DELETE: api/User/5
        [ResponseType(typeof(User))]
        public IHttpActionResult DeleteUser(int id)
        {
            User user = db.Users.Find(id);
            if (user == null)
            {
                return NotFound();
            }

            db.Users.Remove(user);
            db.SaveChanges();

            return Ok(user);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool UserExists(int id)
        {
            return db.Users.Count(e => e.Id == id) > 0;
        }
    }
}