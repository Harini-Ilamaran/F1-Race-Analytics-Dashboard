let drivers = [];
let constructors = [];

let selectedDrivers = [];
let selectedConstructor = null;

Promise.all([

    fetch("/api/drivers").then(response => response.json()),

    fetch("/api/constructors").then(response => response.json())

])

    .then(([driverData, constructorData]) => {

        drivers = driverData;

        constructors = constructorData;

        document.getElementById("driversTab").classList.add("active");

        renderDrivers();

    })

    .catch(error => {

        console.error("Error loading data:", error);

    });

function renderDrivers(search = "") {

    const list = document.getElementById("selectionList");

    list.innerHTML = "";

    drivers.forEach(driver => {

        if(!driver.name.toLowerCase().includes(search)){
            return;
        }

        const alreadySelected =
            selectedDrivers.some(d => d.id === driver.id);

        list.innerHTML += `

        <div class="driver-row">

    <div class="fantasy-driver-left">

        <img
            src="images/drivers/${driver.imageFile}"
            class="driver-photo">

        <div class="driver-details">

            <h3>${driver.name}</h3>

            <div class="driver-price">
    $${driver.cost}M
</div>

        </div>

    </div>

    <div class="fantasy-driver-right">

        <div class="driver-points">

            ${driver.points} Pts

        </div>

        <button
    class="add-btn"
    id="driverBtn${driver.id}"
    onclick="selectDriver(${driver.id})"
    ${alreadySelected ? "disabled" : ""}>

    ${alreadySelected ? "✓" : "+"}

</button>

    </div>

</div>

        `;

    });

}

function renderConstructors(search = "") {

    const list = document.getElementById("selectionList");

    list.innerHTML = "";

    constructors.forEach(constructor => {

        if(!constructor.name.toLowerCase().includes(search)){
            return;
        }

        const selected =
            selectedConstructor &&
            selectedConstructor.id === constructor.id;

        list.innerHTML += `

        <div class="driver-row">

            <img
                src="images/teams/${constructor.imageFile}"
                class="constructor-photo">

            <div class="driver-details">

                <h3>${constructor.name.replace(" F1 Team", "")}</h3>

                <p>$${constructor.cost}M</p>

            </div>

            <div class="driver-points">

                ${constructor.points} Pts

            </div>

            <button
    class="add-btn"
    id="constructorBtn${constructor.id}"
    onclick="selectConstructor(${constructor.id})"
    ${selected ? "disabled" : ""}>

    ${selected ? "✓" : "+"}

</button>

        </div>

        `;

    });

}

function selectDriver(driverId){

    const driver = drivers.find(d => d.id === driverId);

    if(!driver){
        return;
    }

    // Don't allow duplicates
    if(selectedDrivers.some(d => d.id === driver.id)){

        alert("Driver already selected.");

        return;

    }

    // Maximum 2 drivers
    if(selectedDrivers.length >= 2){
        alert("You can only select two drivers.");
        return;
    }

    selectedDrivers.push(driver);

    const btn = document.getElementById("driverBtn" + driver.id);

    if(btn){

        btn.innerHTML = "✓";

        btn.disabled = true;

    }

    updateDriverSlots();
    updateBudget();
    buildFantasyTeam();

}

function updateDriverSlots(){

    const slot1 = document.getElementById("driverSlot1");
    const slot2 = document.getElementById("driverSlot2");

    if(selectedDrivers.length >= 1){

        slot1.innerHTML = `

<div class="selected-driver">

    <button
        class="remove-btn"
        onclick="removeDriver(0)">
        ✕
    </button>

    <img
        src="images/drivers/${selectedDrivers[0].imageFile}"
        class="slot-photo">

    <div class="slot-info">

        <div class="slot-name">

            ${selectedDrivers[0].name}

        </div>

        <div class="slot-cost">

            $${selectedDrivers[0].cost}M

        </div>

    </div>

</div>

`;

    }else{

        slot1.innerHTML = `
            <span>＋</span>
            <p>Add Driver</p>
        `;
    }

    if(selectedDrivers.length >= 2){

        slot2.innerHTML = `

<div class="selected-driver">

    <button
        class="remove-btn"
        onclick="removeDriver(1)">
        ✕
    </button>

    <img
        src="images/drivers/${selectedDrivers[1].imageFile}"
        class="slot-photo">

    <div>

        <div class="slot-name">

            ${selectedDrivers[1].name}

        </div>

        <div class="slot-cost">

            $${selectedDrivers[1].cost}M

        </div>

    </div>

</div>

`;

    }else{

        slot2.innerHTML = `
            <span>＋</span>
            <p>Add Driver</p>
        `;
    }

}

function updateBudget(){

    let total = 0;

    selectedDrivers.forEach(driver => {

        total += driver.cost;

    });

    if(selectedConstructor){

        total += selectedConstructor.cost;

    }

    const remaining = 100 - total;

    document.getElementById("budgetText").innerHTML =
        "$" + remaining.toFixed(1) + "M";

    document.getElementById("budgetFill").style.width =
        remaining + "%";

}

async function buildFantasyTeam(){

    if(selectedDrivers.length !== 2 || !selectedConstructor){

        document.getElementById("analysisCard").innerHTML =
            "Select two drivers and one constructor.";

        return;
    }

    let requestBody = {

        driver1Id: selectedDrivers[0].id,

        driver2Id: selectedDrivers[1].id,

        constructorName: selectedConstructor.name

    };

    let response =
        await fetch("/api/fantasy",{

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body:JSON.stringify(requestBody)

        });

    let result =
        await response.json();

    document.getElementById("analysisCard").innerHTML = `

        <h2>Fantasy Team Analysis</h2>

        <br>

        <h3>Total Cost</h3>

        <p>$${result.totalCost.toFixed(1)}M</p>

        <br>

        <h3>Predicted Points</h3>

        <p>${result.predictedPoints}</p>

        <br>

        <h3>Budget Remaining</h3>

        <p>$${result.budgetRemaining.toFixed(1)}M</p>

        <br>

        <h3>Status</h3>

        <p>${result.status}</p>

    `;

}

function removeDriver(index){

    const removedDriver = selectedDrivers[index];

    selectedDrivers.splice(index,1);

    const btn =
        document.getElementById("driverBtn" + removedDriver.id);

    if(btn){

        btn.innerHTML = "+";

        btn.disabled = false;

    }

    updateDriverSlots();

    updateBudget();

    buildFantasyTeam();

}

function selectConstructor(constructorId){

    const constructor =
        constructors.find(c => c.id === constructorId);

    if(!constructor){
        return;
    }

    // Enable the previously selected constructor button
    if(selectedConstructor){

        const oldBtn =
            document.getElementById("constructorBtn" + selectedConstructor.id);

        if(oldBtn){

            oldBtn.innerHTML = "+";
            oldBtn.disabled = false;

        }

    }

    selectedConstructor = constructor;

    const btn =
        document.getElementById("constructorBtn" + constructor.id);

    if(btn){

        btn.innerHTML = "✓";
        btn.disabled = true;

    }

    updateConstructorSlot();

    updateBudget();

    buildFantasyTeam();

}

function updateConstructorSlot(){

    const slot =
        document.getElementById("constructorSlot");

    if(selectedConstructor){

        slot.innerHTML = `

<div class="selected-driver">

    <button
        class="remove-btn"
        onclick="removeConstructor()">

        ✕

    </button>

    <img
        src="images/teams/${selectedConstructor.imageFile}"
        class="constructor-slot-photo">

    <div class="slot-info">

        <div class="slot-name">

            ${selectedConstructor.name}

        </div>

        <div class="slot-cost">

            $${selectedConstructor.cost}M

        </div>

    </div>

</div>

        `;

    }

    else{

        slot.innerHTML = `

            <span>＋</span>

            <p>Add Constructor</p>

        `;

    }

}

function removeConstructor(){

    if(selectedConstructor){

        const btn =
            document.getElementById("constructorBtn" + selectedConstructor.id);

        if(btn){

            btn.innerHTML = "+";
            btn.disabled = false;

        }

    }

    selectedConstructor = null;

    updateConstructorSlot();

    updateBudget();

    buildFantasyTeam();

}

document.getElementById("driversTab").addEventListener("click", () => {

    document.getElementById("driversTab").classList.add("active");
    document.getElementById("constructorsTab").classList.remove("active");

    document.getElementById("searchBox").value = "";

    renderDrivers();

});

document.getElementById("constructorsTab").addEventListener("click", () => {

    document.getElementById("constructorsTab").classList.add("active");
    document.getElementById("driversTab").classList.remove("active");

    document.getElementById("searchBox").value = "";

    renderConstructors();

});

document.getElementById("searchBox").addEventListener("input", function(){

    const search =
        this.value.toLowerCase();

    if(document.getElementById("driversTab").classList.contains("active")){

        renderDrivers(search);

    }else{

        renderConstructors(search);

    }

});