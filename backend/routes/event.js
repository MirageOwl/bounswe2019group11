const express = require('express');
const eventService = require('../services/event');

const router = express.Router();

router.get('/', async (req, res) => {
    try {
        const Country = req.query.country
        const Rank = req.query.rank
        const SDate = req.query.StartDate
        const response = await eventService.getAll(Country,Rank,SDate);
        res.status(200).json(response);

    } catch (e) {
        res.status(503)
    }
});

router.get('/:id', async (req, res) => {
    try {
        const ID = req.params.id;
        const response = await eventService.getById(ID);
        res.status(200).json(response);
    } catch (e) {
        res.status(503)
    }
});

router.post('/', async (req, res) => {
    try {
        const newEvent = {...req.body};
        const response = await eventService.create(newEvent);
        res.status(200).json(response);
    } catch (e) {
        res.status(503).json(e)
    }
});

module.exports = router;
