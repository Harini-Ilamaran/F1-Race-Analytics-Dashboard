const tyres = [

    {
        code: "SOFT",
        name: "SOFT TYRE",
        image: "images/tyres/soft-tyres.png",
        description: "Maximum grip, quick degradation."
    },

    {
        code: "MEDIUM",
        name: "MEDIUM TYRE",
        image: "images/tyres/medium-tyres.png",
        description: "Balanced speed and durability."
    },

    {
        code: "HARD",
        name: "HARD TYRE",
        image: "images/tyres/hard-tyres.png",
        description: "Endurance, stable but less grip."
    },

    {
        code: "INTERMEDIATE",
        name: "INTERMEDIATE TYRE",
        image: "images/tyres/intermediate-tyres.png",
        description: "Versatile wet condition performance."
    },

    {
        code: "FULL_WET",
        name: "FULL WET TYRE",
        image: "images/tyres/fullwet-tyres.png",
        description: "Extreme rain grip, displaces water."
    }

];

let currentTyre = 0;

async function simulatePitStop() {

    let position =
        document.getElementById(
            "position"
        ).value;

    let tire =
        document.getElementById(
            "tire"
        ).value;


    let lapsRemaining =
        document.getElementById(
            "lapsRemaining"
        ).value;

    let requestBody = {

        position: parseInt(position),

        tire: tire,

        lapsRemaining:
            parseInt(lapsRemaining)
    };

    let response =
        await fetch(
            "/api/pitstop",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(
                        requestBody
                    )
            }
        );

    let result =
        await response.json();

    document.getElementById(
        "strategyResult"
    ).innerHTML =

        `
        <h2>Strategy:</h2>
        ${result.strategy}
        <br><br>

        <h2>Pit Window:</h2>
        ${result.pitWindow}
        <br><br>

        <h2>Estimated Finish:</h2>
        ${result.estimatedFinish}
        `;
}

async function updateTyreAnalysis(tireCode) {

    let response =
        await fetch(
            "/api/tire",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    tireType: tireCode

                })
            }
        );

    let result =
        await response.json();

    document.getElementById(
        "tyreAnalysis"
    ).innerHTML =

        `
        <p>Performance Decline:</p>
        ${result.performanceDecline}

        <br><br>

        <p>Tyre Life:</p>
        ${result.tireLife}

        <br><br>

        <p>Recommended Pit Window:</p>
        ${result.pitWindow}
        `;
}

function updateTyreCard() {

    const center = tyres[currentTyre];

    updateTyreAnalysis(center.code);

    const left =
        tyres[
        (currentTyre - 1 + tyres.length)
        % tyres.length
            ];

    const right =
        tyres[
        (currentTyre + 1)
        % tyres.length
            ];

    document.getElementById(
        "centerCard"
    ).innerHTML = `

    <input
        type="hidden"
        id="tire"
        value="${center.code}">

    <span class="card-name">
        ${center.name}
    </span>

    <img
        src="${center.image}"
        class="card-image"
    >

    <p>
        ${center.description}
    </p>
`;

    document.getElementById(
        "leftCard"
    ).innerHTML = `
        <img
            src="${left.image}"
            class="side-image"
        >
    `;

    document.getElementById(
        "rightCard"
    ).innerHTML = `
        <img
            src="${right.image}"
            class="side-image"
        >
    `;

}

document.getElementById(
    "nextTire"
).addEventListener(
    "click",
    () => {

        console.log("RIGHT CLICK");

        currentTyre++;

        if (
            currentTyre >= tyres.length
        ) {
            currentTyre = 0;
        }

        updateTyreCard();
    }
);

document.getElementById(
    "prevTire"
).addEventListener(
    "click",
    () => {

        currentTyre--;

        if (
            currentTyre < 0
        ) {
            currentTyre =
                tyres.length - 1;
        }

        updateTyreCard();
    }
);

updateTyreCard();