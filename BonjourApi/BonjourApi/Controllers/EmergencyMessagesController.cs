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

namespace BonjourApi.Controllers
{
    public class EmergencyMessagesController : ApiController
    {
        private BonjourApiContext db = new BonjourApiContext();

        // GET: api/EmergencyMessages
        public IQueryable<EmergencyMessage> GetEmergencyMessages()
        {
            return db.EmergencyMessages;
        }

        // GET: api/EmergencyMessages/5
        [ResponseType(typeof(EmergencyMessage))]
        public IHttpActionResult GetEmergencyMessage(int id)
        {
            //EmergencyMessage emergencyMessage = db.EmergencyMessages.Find(id);
            List<EmergencyMessage> emergencyMessage = db.EmergencyMessages.Where(e => e.receiverId == id).ToList();
            if (emergencyMessage.Count>0)
            {
                return Ok(emergencyMessage);
            }
            else
            {
                return NotFound();
            }
        }

        // PUT: api/EmergencyMessages/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutEmergencyMessage(int id, EmergencyMessage emergencyMessage)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != emergencyMessage.Id)
            {
                return BadRequest();
            }

            db.Entry(emergencyMessage).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!EmergencyMessageExists(id))
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

        // POST: api/EmergencyMessages
        [ResponseType(typeof(EmergencyMessage))]
        public IHttpActionResult PostEmergencyMessage(EmergencyMessage emergencyMessage)
        {
            List<int> nearbyUsers = new List<int>();
            if (!UserExists(emergencyMessage.senderId))
            {
                return null;
            }
            else
            {
                int distance = 2;//Utility.getRadarAera(area);
                var user = db.Users.FirstOrDefault(u => u.Id == emergencyMessage.senderId);
                
                foreach (var u in db.Users)
                {
                    if (u.Id != emergencyMessage.senderId)
                    {
                        if (Utility.distFrom(user.latitude, user.longitude, u.latitude, u.longitude) <= distance)
                        {
                            nearbyUsers.Add(u.Id);
                        }
                    }
                }
                foreach (int u in nearbyUsers)
                {
                    emergencyMessage.receiverId = u;
                    db.EmergencyMessages.Add(emergencyMessage);
                    db.SaveChanges();
                }
            }

            return Ok(nearbyUsers.Count);
        }

        // DELETE: api/EmergencyMessages/5
        [ResponseType(typeof(EmergencyMessage))]
        public IHttpActionResult DeleteEmergencyMessage(int id)
        {
            EmergencyMessage emergencyMessage = db.EmergencyMessages.Find(id);
            if (emergencyMessage == null)
            {
                return NotFound();
            }

            db.EmergencyMessages.Remove(emergencyMessage);
            db.SaveChanges();

            return Ok(emergencyMessage);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool EmergencyMessageExists(int id)
        {
            return db.EmergencyMessages.Count(e => e.Id == id) > 0;
        }
        private bool UserExists(int id)
        {
            return db.Users.Count(e => e.Id == id) > 0;
        }
    }
}