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

let allDrivers = [];

fetch("http://localhost:8080/api/drivers")

    .then(response => response.json())

    .then(drivers => {

        allDrivers = drivers;

        let driver1 =
            document.getElementById("driver1");

        let driver2 =
            document.getElementById("driver2");

        drivers.forEach(driver => {

            driver1.innerHTML +=
                `<option value="${driver.id}">
            ${driver.name}
        </option>`;

            driver2.innerHTML +=
                `<option value="${driver.id}">
            ${driver.name}
        </option>`;
        });

    });

let chart;

function compareDrivers(){

    let id1 =
        document.getElementById(
            "driver1"
        ).value;

    let id2 =
        document.getElementById(
            "driver2"
        ).value;

    if(id1 == id2){

        alert(
            "Please select two different drivers."
        );

        return;
    }

    document.getElementById(
        "driverCards"
    ).style.display = "grid";

    document.querySelector(
        ".chart-card"
    ).style.display = "block";

    document.getElementById(
        "comparisonSummary"
    ).style.display = "block";

    let d1 =
        allDrivers.find(
            driver =>
                driver.id == id1
        );

    let d2 =
        allDrivers.find(
            driver =>
                driver.id == id2
        );

    console.log(d1);
    console.log(d2);

    const color1 = teamColors[d1.team] || "#c40000";
    const color2 = teamColors[d2.team] || "#c40000";

    document.getElementById(
        "driverCards"
    ).innerHTML = `

<div class="compare-driver-card"
     style="
        border-top:4px solid ${color1};
        border-left:4px solid ${color1};
        box-shadow:0 0 25px ${color1}66;
     ">

    <img src="images/drivers/${d1.imageFile}">

    <h3>${d1.name}</h3>

    <p>Team: ${d1.team}</p>

    <p>Points: ${d1.points}</p>

</div>

<div class="compare-driver-card"
     style="
        border-top:4px solid ${color2};
        border-left:4px solid ${color2};
        box-shadow:0 0 25px ${color2}66;
     ">

    <img src="images/drivers/${d2.imageFile}">

    <h3>${d2.name}</h3>

    <p>Team: ${d2.team}</p>

    <p>Points: ${d2.points}</p>

</div>

`;

    let ctx =
        document.getElementById(
            "comparisonChart"
        );

    if(chart){
        chart.destroy();
    }

    chart =
        new Chart(ctx,{

            type:'bar',

            data:{

                labels:[
                    'Wins',
                    'Podiums',
                    'Poles',
                    'Fastest Laps',
                    'Points'
                ],

                datasets:[
                    {
                        label:d1.name,

                        data:[
                            d1.wins,
                            d1.podiums,
                            d1.poles,
                            d1.fastestLaps,
                            d1.points
                        ],

                    },

                    {
                        label:d2.name,

                        data:[
                            d2.wins,
                            d2.podiums,
                            d2.poles,
                            d2.fastestLaps,
                            d2.points
                        ],

                    }
                ]
            },

            options:{
                responsive:true,
                maintainAspectRatio:false,

                plugins:{
                    legend:{
                        labels:{
                            color:'white'
                        }
                    }
                },

                scales:{
                    x:{
                        ticks:{
                            color:'white'
                        }
                    },

                    y:{
                        beginAtZero:true,

                        ticks:{
                            color:'white'
                        }
                    }
                }
            }

        });


    let leader =
        d1.points > d2.points
            ? d1
            : d2;

    let gap =
        Math.abs(
            d1.points -
            d2.points
        );

    let trailingDriver =
        leader.id == d1.id
            ? d2.name
            : d1.name;

    document.getElementById(
        "comparisonSummary"
    ).innerHTML = `

<div class="summary-card">

    <p>
        ${leader.name} leads ${trailingDriver}
        by ${gap} points. Currently showing stronger
        championship performance.
    </p>

</div>

`;
}

