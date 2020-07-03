﻿using System;
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
    public class LiftsController : ApiController
    {
        private BonjourApiContext db = new BonjourApiContext();

        // GET: api/Lifts
        public IQueryable<Lift> GetLifts()
        {
            return db.Lifts;
        }

        // GET: api/Lifts/5
        [ResponseType(typeof(Lift))]
        public IHttpActionResult GetLift(int id)
        {
            Lift lift = db.Lifts.Find(id);
            if (lift == null)
            {
                return NotFound();
            }

            return Ok(lift);
        }

        // PUT: api/Lifts/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutLift(int id, Lift lift)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != lift.Id)
            {
                return BadRequest();
            }

            db.Entry(lift).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!LiftExists(id))
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

        // POST: api/Lifts
        [ResponseType(typeof(Lift))]
        public IHttpActionResult PostLift(Lift lift)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Lifts.Add(lift);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = lift.Id }, lift);
        }

        // DELETE: api/Lifts/5
        [ResponseType(typeof(Lift))]
        public IHttpActionResult DeleteLift(int id)
        {
            Lift lift = db.Lifts.Find(id);
            if (lift == null)
            {
                return NotFound();
            }

            db.Lifts.Remove(lift);
            db.SaveChanges();

            return Ok(lift);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool LiftExists(int id)
        {
            return db.Lifts.Count(e => e.Id == id) > 0;
        }
    }
}