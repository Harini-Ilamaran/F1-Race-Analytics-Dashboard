let drivers = [];

fetch("/api/drivers")

    .then(response => response.json())

    .then(data => {

        drivers = data;

        let driver1 =
            document.getElementById(
                "driver1"
            );

        let driver2 =
            document.getElementById(
                "driver2"
            );

        data.forEach(driver => {

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

async function buildFantasyTeam(){

    let driver1Id =
        parseInt(
            document.getElementById(
                "driver1"
            ).value
        );

    let driver2Id =
        parseInt(
            document.getElementById(
                "driver2"
            ).value
        );

    if(driver1Id === driver2Id){

        document.getElementById(
            "fantasyResult"
        ).innerHTML =

            `
        <strong>
        Please select two different drivers.
        </strong>
        `;

        return;
    }

    let requestBody = {

        driver1Id: driver1Id,

        driver2Id: driver2Id,

        constructorName:
        document.getElementById(
            "constructor"
        ).value
    };

    let response =
        await fetch(
            "/api/fantasy",
            {
                method:"POST",

                headers:{
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
        "fantasyResult"
    ).innerHTML =

        `
        <strong>Total Cost:</strong>
        $${result.totalCost}

        <br><br>

        <strong>Predicted Points:</strong>
        ${result.predictedPoints}

        <br><br>

        <strong>Budget Remaining:</strong>
        $${result.budgetRemaining}

        <br><br>

        <strong>Status:</strong>
        ${result.status}
        `;
}