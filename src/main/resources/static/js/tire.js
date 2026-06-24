async function predictTire() {

    let tireType =
        document.getElementById(
            "tireType"
        ).value;

    let response =
        await fetch(
            "/api/tire",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body: JSON.stringify({
                    tireType: tireType
                })
            }
        );

    let result =
        await response.json();

    document.getElementById(
        "tireResult"
    ).innerHTML =

        `
        <strong>
        Performance Decline:
        </strong>

        ${result.performanceDecline}

        <br><br>

        <strong>
        Tire Life:
        </strong>

        ${result.tireLife}

        <br><br>

        <strong>
        Recommended Pit Window:
        </strong>

        ${result.pitWindow}
        `;
}