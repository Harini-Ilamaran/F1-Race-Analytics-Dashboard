function getLogo(teamName) {

    const logos = {

        "Mercedes":
            "images/teams/mercedes.png",

        "Ferrari":
            "images/teams/ferrari.png",

        "McLaren":
            "images/teams/mclaren.png",

        "Red Bull":
            "images/teams/redbull.png",

        "Alpine F1 Team":
            "images/teams/alpine.png",

        "RB F1 Team":
            "images/teams/racingbulls.png",

        "Haas F1 Team":
            "images/teams/haas.png",

        "Williams":
            "images/teams/williams.png",

        "Audi":
            "images/teams/audi.png",

        "Aston Martin":
            "images/teams/astonmartin.png",

        "Cadillac F1 Team":
            "images/teams/cadillac.png"
    };

    return logos[teamName];
}

fetch("/api/constructors")
    .then(response => response.json())
    .then(data => {

        data.sort(
            (a, b) => b.points - a.points
        );

        let table =
            document.getElementById(
                "constructorTable"
            );

        table.innerHTML =
            `
            <tr>
                <th>Rank</th>
                <th>Team</th>
                <th>Points</th>
                <th>Wins</th>
            </tr>
            `;

        data.forEach(
            (team, index) => {

                table.innerHTML +=
                    `
                    <tr>
                        <td>${index + 1}</td>
                        <td>
    <img
        src="${getLogo(team.name)}"
        class="team-logo"
        alt="${team.name}"
    >
    ${team.name.replace(" F1 Team", "")}
</td>
                        <td>${team.points}</td>
                        <td>${team.wins}</td>
                    </tr>
                    `;
            }
        );
    });

