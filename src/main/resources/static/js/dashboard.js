const driverImages = {

    "Lando Norris":
        "/images/drivers/lando_norris.png",

    "Oscar Piastri":
        "/images/drivers/oscar_piastri.png",

    "Charles Leclerc":
        "/images/drivers/charles_leclerc.png",

    "Lewis Hamilton":
        "/images/drivers/lewis_hamilton.png",

    "Max Verstappen":
        "/images/drivers/max_verstappen.png",

    "Isack Hadjar":
        "/images/drivers/isack_hadjar.png",

    "George Russell":
        "/images/drivers/george_russell.png",

    "Kimi Antonelli":
        "/images/drivers/kimi_antonelli.png",

    "Fernando Alonso":
        "/images/drivers/fernando_alonso.png",

    "Lance Stroll":
        "/images/drivers/lance_stroll.png",

    "Sergio Perez":
        "/images/drivers/sergio_perez.png",

    "Valtteri Bottas":
        "/images/drivers/valtteri_bottas.png",

    "Nico Hulkenberg":
        "/images/drivers/nico_hulkenberg.png",

    "Gabriel Bortoleto":
        "/images/drivers/gabriel_bortoleto.png",

    "Carlos Sainz":
        "/images/drivers/carlos_sainz.png",

    "Alexander Albon":
        "/images/drivers/alexander_albon.png",

    "Pierre Gasly":
        "/images/drivers/pierre_gasly.png",

    "Franco Colapinto":
        "/images/drivers/franco_colapinto.png",

    "Esteban Ocon":
        "/images/drivers/esteban_ocon.png",

    "Oliver Bearman":
        "/images/drivers/oliver_bearman.png",

    "Liam Lawson":
        "/images/drivers/liam_lawson.png",

    "Arvid Lindblad":
        "/images/drivers/arvid_lindblad.png",

};

const teamColors = {

    "McLaren": "#FF8000",
    "Ferrari": "#DC0000",
    "Mercedes": "#00D2BE",
    "Red Bull": "#0600EF",
    "Williams": "#00A3E0",
    "Aston Martin": "#006F62",
    "Alpine": "#0090FF",
    "Haas": "#B6BABD",
    "Racing Bulls": "#1E5BC6",
    "Audi": "#E30613",
    "Cadillac": "#B6BABD"
};

const teamLogos = {

    "McLaren": "/images/teams/mclaren.png",
    "Ferrari": "/images/teams/ferrari.png",
    "Mercedes": "/images/teams/mercedes.png",
    "Red Bull": "/images/teams/redbull.png",
    "Williams": "/images/teams/williams.png",
    "Aston Martin": "/images/teams/astonmartin.png",
    "Alpine": "/images/teams/alpine.png",
    "Haas": "/images/teams/haas.png",
    "Racing Bulls": "/images/teams/racingbulls.png",
    "Audi": "/images/teams/audi.png",
    "Cadillac": "/images/teams/cadillac.png"
};

const carImages = {

    "McLaren":
        "/images/cars/mclaren_car.png",

    "Ferrari":
        "/images/cars/ferrari_car.png",

    "Mercedes":
        "/images/cars/mercedes_car.png",

    "Red Bull":
        "/images/cars/redbull_car.png",

    "Williams":
        "/images/cars/williams_car.png",

    "Aston Martin":
        "/images/cars/astonmartin_car.png",

    "Alpine":
        "/images/cars/alpine_car.png",

    "Haas":
        "/images/cars/haas_car.png",

    "Racing Bulls":
        "/images/cars/racingbulls_car.png",

    "Audi":
        "/images/cars/audi_car.png",

    "Cadillac":
        "/images/cars/cadillac_car.png"
};

const driverDetails = {

    "Oscar Piastri": {
        number: 81
    },

    "Lando Norris": {
        number: 4
    },

    "Charles Leclerc": {
        number: 16
    },

    "Lewis Hamilton": {
        number: 44
    },

    "Max Verstappen": {
        number: 1
    },

    "George Russell": {
        number: 63
    },

    "Kimi Antonelli": {
        number: 12
    },

    "Fernando Alonso": {
        number: 14
    },

    "Lance Stroll": {
        number: 18
    },

    "Carlos Sainz": {
        number: 55
    },

    "Alexander Albon": {
        number: 23
    },

    "Pierre Gasly": {
        number: 10
    },

    "Esteban Ocon": {
        number: 31
    },

    "Oliver Bearman": {
        number: 87
    },

    "Liam Lawson": {
        number: 30
    },

    "Isack Hadjar": {
        number: 6
    },

    "Gabriel Bortoleto": {
        number: 5
    },

    "Franco Colapinto": {
        number: 43
    },

    "Valtteri Bottas": {
        number: 77
    },

    "Nico Hulkenberg": {
        number: 27
    },

    "Sergio Perez": {
        number: 11
    },

    "Arvid Lindblad": {
        number: 29
    },

    "Yuki Tsunoda": {
        number: 22
    }
};


fetch("/api/drivers")
    .then(response => response.json())
    .then(drivers => {

        drivers.sort(
            (a, b) => b.points - a.points
        );

        document.getElementById(
            "leaderName"
        ).innerText =
            drivers[0].name +
            " (" +
            drivers[0].points +
            " pts)";

        document.getElementById("leaderImage").src =
            driverImages[drivers[0].name];

        document.getElementById(
            "totalDrivers"
        ).innerText =
            drivers.length;

        let teams =
            [...new Set(
                drivers.map(
                    driver => driver.team
                )
            )];

        document.getElementById(
            "totalTeams"
        ).innerText =
            teams.length;

        document.getElementById(
            "fastestDriver"
        ).innerText =
            drivers[0].name;

        let maxWinsDriver =
            drivers.reduce(
                (max, current) =>
                    current.wins > max.wins
                        ? current
                        : max
            );

        fetch("/api/constructors")
            .then(response => response.json())
            .then(constructors => {

                constructors.sort(
                    (a, b) => b.points - a.points
                );

                const leaderTeam =
                    constructors[0];

                document.getElementById(
                    "constructorLogo"
                ).src =
                    teamLogos[leaderTeam.name];

                document.getElementById(
                    "constructorLeader"
                ).innerText =
                    leaderTeam.name +
                    " (" +
                    leaderTeam.points +
                    " pts)";
            });

        let table =
            document.getElementById(
                "driverTableBody"
            );

        let openedCard = null;

        drivers.forEach(
            (driver, index) => {

                let row =
                    document.createElement("tr");

                row.innerHTML = `
    <td>${index + 1}</td>

    <td>
        ${driver.name}
        <span class="expand-arrow">
            ▼
        </span>
    </td>

    <td>${driver.team}</td>

    <td>${driver.points}</td>
`;

                table.appendChild(row);

                let detailRow =
                    document.createElement("tr");

                detailRow.classList.add(
                    "driver-details-row"
                );

                detailRow.style.display =
                    "none";

                detailRow.innerHTML = `
<td colspan="4">

<div
    class="driver-expand-card"
    style="
        border-left:5px solid ${teamColors[driver.team]};
        box-shadow:0 0 20px ${teamColors[driver.team]}55;
    "
>

    <div class="driver-left">

        <img
            src="${driverImages[driver.name]}"
            alt="${driver.name}"
        >

    </div>

    <div class="driver-center">

    <div class="driver-title">

    <img
        src="${teamLogos[driver.team]}"
        class="team-logo"
        alt="${driver.team}"
    >

    <h2>${driver.name}</h2>

    <div class="driver-number">
        #${driverDetails[driver.name]?.number}
    </div>

</div>

    <p>Team: ${driver.team}</p>

    <p>Points: ${driver.points}</p>

    <p>Wins: ${driver.wins}</p>

</div>

    <div class="driver-right">

        <img
            src="${carImages[driver.team]}"
            alt="${driver.team}"
        >

    </div>

</div>

</td>
`;

                table.appendChild(detailRow);

                row.addEventListener(
                    "click",
                    () => {

                        if (
                            openedCard &&
                            openedCard !== detailRow
                        ) {
                            openedCard.style.display =
                                "none";
                        }

                        if (
                            detailRow.style.display ===
                            "table-row"
                        ) {

                            detailRow.style.display =
                                "none";

                            openedCard = null;

                        } else {

                            detailRow.style.display =
                                "table-row";

                            openedCard =
                                detailRow;
                        }
                    }
                );
            }
        );
    });

let raceDate;

function updateCountdown() {

    if (!raceDate) {
        return;
    }

    const now =
        new Date();

    const difference =
        raceDate - now;

    const days =
        Math.floor(
            difference /
            (1000 * 60 * 60 * 24)
        );

    const hours =
        Math.floor(
            (
                difference %
                (1000 * 60 * 60 * 24)
            ) /
            (1000 * 60 * 60)
        );

    const minutes =
        Math.floor(
            (
                difference %
                (1000 * 60 * 60)
            ) /
            (1000 * 60)
        );

    const seconds =
        Math.floor(
            (
                difference %
                (1000 * 60)
            ) /
            1000
        );

    document.getElementById("days")
        .innerText = days;

    document.getElementById("hours")
        .innerText = hours;

    document.getElementById("minutes")
        .innerText = minutes;

    document.getElementById("seconds")
        .innerText = seconds;
}

updateCountdown();

fetch(
    "/api/drivers/race-info"
)
    .then(response => response.json())
    .then(race => {

        document.getElementById(
            "raceName"
        ).innerText =
            race.raceName;

        document.getElementById(
            "raceCircuit"
        ).innerText =
            race.circuit;

        document.getElementById(
            "racesCompleted"
        ).innerText =
            race.racesCompleted;

        raceDate =
            new Date(
                race.raceDate + "T" + race.raceTime
            );

        updateCountdown();

    });

setInterval(
    updateCountdown,
    1000
);

fetch("/api/constructors")
    .then(response => response.json())
    .then(constructors => {

        constructors.sort(
            (a, b) => b.points - a.points
        );

        console.log(
            "Constructors Loaded:"
        );

        constructors.forEach(c => {

            console.log(
                c.name +
                " : " +
                c.points
            );

        });

    });



