CREATE OR REPLACE PUBLIC SYNONYM syn_pkmn FOR pokemon;
CREATE OR REPLACE PUBLIC SYNONYM syn_pkmn_paste FOR pokemon_paste;
CREATE OR REPLACE PUBLIC SYNONYM syn_move FOR move;
CREATE OR REPLACE PUBLIC SYNONYM syn_mvcat FOR move_cat;
CREATE OR REPLACE PUBLIC SYNONYM syn_gdr FOR gender;
CREATE OR REPLACE PUBLIC SYNONYM syn_itm FOR item;
CREATE OR REPLACE PUBLIC SYNONYM syn_pkmn_ab FOR pkmn_ability;
CREATE OR REPLACE PUBLIC SYNONYM syn_abi FOR ability;
CREATE OR REPLACE PUBLIC SYNONYM syn_nat FOR nature;
CREATE OR REPLACE PUBLIC SYNONYM syn_pkmn_t FOR pokemon_type_combination;
CREATE OR REPLACE PUBLIC SYNONYM syn_type FOR pokemon_type;
--INDEX CREATION AS POKEMON_POKEPASTE USER

describe pkmn_ability;

CREATE UNIQUE INDEX idx_pokemon_name ON pokemon(name);
CREATE UNIQUE INDEX idx_move_name_description ON move(name);
CREATE UNIQUE INDEX idx_items_name_description ON item(name, description);
CREATE UNIQUE INDEX idx_ability_name_description ON ability(name, description);
CREATE UNIQUE INDEX idx_nature_name ON nature(name);
CREATE UNIQUE INDEX idx_type_name ON pokemon_type(name);

--VIEW CREATION
--POBLAR TABLAS
SELECT id, name FROM syn_type;

CREATE OR REPLACE VIEW V_GET_PKMN AS
SELECT * FROM syn_pkmn ORDER BY id;

    SELECT * FROM pokemon_pokepaste.V_GET_PKMN;

CREATE OR REPLACE VIEW V_GET_MOVES AS
SELECT * FROM syn_move ORDER BY name;

    SELECT * FROM pokemon_pokepaste.V_GET_MOVES;

CREATE OR REPLACE VIEW V_GET_ITEMS AS
SELECT * FROM syn_itm ORDER BY name;

    SELECT * FROM pokemon_pokepaste.V_GET_ITEMS;

CREATE OR REPLACE VIEW V_GET_ABILITIES AS
SELECT * FROM syn_abi ORDER BY name;

    SELECT * FROM pokemon_pokepaste.V_GET_ABILITIES;

CREATE OR REPLACE VIEW V_GET_PKMN_TYPES AS
SELECT * FROM syn_type ORDER BY name;

    SELECT * FROM pokemon_pokepaste.V_GET_PKMN_TYPES;

CREATE OR REPLACE VIEW V_GET_NATURES AS
SELECT * FROM syn_nat ORDER BY id;

    SELECT * FROM pokemon_pokepaste.V_GET_NATURES;

--VIEW CREATION
--INTERACTUAR POKEPASTE

CREATE OR REPLACE VIEW V_GET_POKEPASTE AS
SELECT
    pp.id AS paste_id,
    pp.nickname,
    pp."level",
    CASE pp.is_shiny
        WHEN 0 THEN 'No'
        ELSE 'Yes'
    END AS is_shiny,
    p.name AS pokemon_name,
    t.name AS tera_type_name,
    a.name AS ability_name,
    i.name AS item_name,
    g.name AS gender_name,
    m1.name AS move1_name,
    m2.name AS move2_name,
    m3.name AS move3_name,
    m4.name AS move4_name,
    n.name AS nature_name,
    pp.hp,
    pp.atk,
    pp."def",
    pp.spatk,
    pp.spdef,
    pp.spd
FROM
    syn_pkmn_paste pp
    INNER JOIN syn_pkmn p ON pp.id_pokemon = p.id
    INNER JOIN syn_type t ON pp.id_tera_type = t.id
    INNER JOIN syn_abi a ON pp.id_ability = a.id
    LEFT JOIN syn_itm i ON pp.id_item = i.id
    INNER JOIN syn_gdr g ON pp.id_gender = g.id
    INNER JOIN syn_move m1 ON pp.move1 = m1.id
    LEFT JOIN syn_move m2 ON pp.move2 = m2.id
    LEFT JOIN syn_move m3 ON pp.move3 = m3.id
    LEFT JOIN syn_move m4 ON pp.move4 = m4.id
    INNER JOIN syn_nat n ON pp.id_nature = n.id
ORDER BY
    pp.id;
    
SELECT * FROM pokemon_pokepaste.V_GET_POKEPASTE;    
    
CREATE OR REPLACE VIEW V_GET_MOVE_TABLE AS
SELECT
    m.id AS move_id,
    m.name AS move_name,
    INITCAP(mc.name) AS move_category,
    mt.name AS move_type,
    NVL(TO_CHAR(m.power), '—') AS power,
    NVL(TO_CHAR(m.accuracy), '—') AS accuracy,
    NVL(TO_CHAR(m.pp), '—') AS pp,
    m.effect,
    NVL(TO_CHAR(m.effect_prob), '—') AS effect_prob
FROM
    syn_move m
INNER JOIN
    syn_type mt ON m.id_type = mt.id
INNER JOIN
    syn_mvcat mc ON m.id_move_cat = mc.id;

SELECT * FROM pokemon_pokepaste.V_GET_MOVE_TABLE;    