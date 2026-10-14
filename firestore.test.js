const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read teams", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("teams").get());
});

test("Bob cannot read Alice's teams", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).collection("teams").doc("team_1").set({
      name: "ALPHA",
      position: 1,
      pj: 0,
      g: 0,
      e: 0,
      p: 0,
      gf: 0,
      gc: 0,
      dg: 0,
      pts: 0,
      createdAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("teams").doc("team_1").get());
});

test("Alice can create and read her own team", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const teamRef = aliceDb.collection("users").doc(ALICE_UID).collection("teams").doc("team_1");

  await assertSucceeds(teamRef.set({
    name: "ALPHA",
    shortName: "ALP",
    shieldColor: "#7E22CE",
    position: 1,
    pj: 0,
    g: 0,
    e: 0,
    p: 0,
    gf: 0,
    gc: 0,
    dg: 0,
    pts: 0,
    createdAt: new Date(),
  }));

  await assertSucceeds(teamRef.get());
});

test("Alice cannot create team with negative points", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const teamRef = aliceDb.collection("users").doc(ALICE_UID).collection("teams").doc("team_invalid");

  await assertFails(teamRef.set({
    name: "ALPHA",
    position: 1,
    pj: 0,
    g: 0,
    e: 0,
    p: 0,
    gf: 0,
    gc: 0,
    dg: 0,
    pts: -5,
    createdAt: new Date(),
  }));
});

test("Alice can create a match, Bob cannot update it", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const matchRef = aliceDb.collection("users").doc(ALICE_UID).collection("matches").doc("match_1");

  await assertSucceeds(matchRef.set({
    round: 1,
    homeTeamId: "team_1",
    homeTeamName: "ALPHA",
    awayTeamId: "team_2",
    awayTeamName: "CAIMANES FC",
    homeScore: 0,
    awayScore: 0,
    status: "scheduled",
    createdAt: new Date(),
  }));

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const bobsMatchRef = bobDb.collection("users").doc(ALICE_UID).collection("matches").doc("match_1");
  await assertFails(bobsMatchRef.update({
    homeScore: 2,
    updatedAt: new Date(),
  }));
});
