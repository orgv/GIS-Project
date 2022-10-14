
var styleJSON = {
    "version": 8,
    "name": "qgis2web export",
    "pitch": 0,
    "light": {
        "intensity": 0.2
    },
    "sources": {
        "OSMStandard_0": {
            "type": "raster",
            "tiles": ["http://tile.openstreetmap.org/{z}/{x}/{y}.png"],
            "tileSize": 256
        },
        "gis_osm_places_a_free_1_1": {
            "type": "geojson",
            "data": json_gis_osm_places_a_free_1_1
        }
                    },
    "sprite": "",
    "glyphs": "https://glfonts.lukasmartinelli.ch/fonts/{fontstack}/{range}.pbf",
    "layers": [
        {
            "id": "background",
            "type": "background",
            "layout": {},
            "paint": {
                "background-color": "#ffffff"
            }
        },
        {
            "id": "lyr_OSMStandard_0_0",
            "type": "raster",
            "source": "OSMStandard_0"
        },
        {
            "id": "lyr_gis_osm_places_a_free_1_1_0",
            "type": "fill",
            "source": "gis_osm_places_a_free_1_1",
            "layout": {},
            "paint": {'fill-opacity': 0.5, 'fill-color': '#8c1dc4'}
        }
],
}